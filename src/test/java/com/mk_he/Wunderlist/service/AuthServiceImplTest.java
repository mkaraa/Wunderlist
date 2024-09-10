package com.mk_he.Wunderlist.service;

import com.mk_he.Wunderlist.config.security.JwtUtil;
import com.mk_he.Wunderlist.domain.entity.User;
import com.mk_he.Wunderlist.domain.request.AuthenticationRequest;
import com.mk_he.Wunderlist.domain.request.RegisterUserRequest;
import com.mk_he.Wunderlist.domain.response.ResponseDto;
import com.mk_he.Wunderlist.exception.PasswordContextInvalidException;
import com.mk_he.Wunderlist.exception.PasswordLengthInvalidException;
import com.mk_he.Wunderlist.exception.UsernameLengthInvalidException;
import com.mk_he.Wunderlist.repository.UserRepository;
import com.mk_he.Wunderlist.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ValidationService validationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void registerUser_whenUserDoesNotExist_thenSuccess() {
        RegisterUserRequest request = new RegisterUserRequest("username", "password");
        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        ResponseDto<String> response = authService.registerUser(request);

        assertTrue(response.isSuccess());
        assertEquals("User registered successfully", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_whenUserAlreadyExists_thenFailure() {
        RegisterUserRequest request = new RegisterUserRequest("username", "password");
        User existingUser = new User();
        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(existingUser));

        ResponseDto<String> response = authService.registerUser(request);

        assertFalse(response.isSuccess());
        assertEquals("User already exists with username: username", response.getMessage());
    }

    @Test
    void registerUser_whenUsernameLengthInvalid_thenFailure() {
        RegisterUserRequest request = new RegisterUserRequest("short", "validPassword123");
        doThrow(new UsernameLengthInvalidException()).when(validationService).validateUsername(anyString());

        ResponseDto<String> response = authService.registerUser(request);

        assertFalse(response.isSuccess());
        assertEquals(UsernameLengthInvalidException.ERROR_MESSAGE, response.getMessage());
    }

    @Test
    void registerUser_whenPasswordLengthInvalid_thenFailure() {
        RegisterUserRequest request = new RegisterUserRequest("validUsername", "short");
        doThrow(new PasswordLengthInvalidException()).when(validationService).validatePassword(anyString());

        ResponseDto<String> response = authService.registerUser(request);

        assertFalse(response.isSuccess());
        assertEquals("Password should contain at least 1 lower and 1 upper case letter and Password length should be grater than 8 and lower then 20. ", response.getMessage());
    }

    @Test
    void registerUser_whenPasswordContextInvalid_thenFailure() {
        RegisterUserRequest request = new RegisterUserRequest("validUsername", "invalidPassword");
        doThrow(new PasswordContextInvalidException()).when(validationService).validatePassword(anyString());

        ResponseDto<String> response = authService.registerUser(request);

        assertFalse(response.isSuccess());
        assertEquals("Password should contain at least 1 lower and 1 upper case letter and Password length should be grater than 8 and lower then 20. ", response.getMessage());
    }

    @Test
    void registerUser_whenUnexpectedException_thenFailure() {
        RegisterUserRequest request = new RegisterUserRequest("validUsername", "validPassword123");
        doThrow(new RuntimeException("Unexpected error")).when(validationService).validateUsername(anyString());

        ResponseDto<String> response = authService.registerUser(request);

        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().startsWith("Unexpected error occurred:"));
    }

    @Test
    void authenticateUser_whenAuthenticationSuccessful_thenSuccess() {
        AuthenticationRequest request = new AuthenticationRequest("username", "password");
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtil.GenerateToken(anyString())).thenReturn("token");

        ResponseDto<String> response = authService.authenticateUser(request);

        assertTrue(response.isSuccess());
        assertEquals("User authenticated successfully", response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    void authenticateUser_whenAuthenticationFails_thenFailure() {
        AuthenticationRequest request = new AuthenticationRequest("username", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {
                });
        ResponseDto<String> responseDto = authService.authenticateUser(request);
        assertFalse(responseDto.isSuccess());
        assertEquals("Invalid credentials", responseDto.getMessage());
        assertNull(responseDto.getData());
    }

}
