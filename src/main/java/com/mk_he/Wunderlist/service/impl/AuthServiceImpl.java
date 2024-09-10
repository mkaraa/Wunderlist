package com.mk_he.Wunderlist.service.impl;

import com.mk_he.Wunderlist.config.security.JwtUtil;
import com.mk_he.Wunderlist.domain.entity.User;
import com.mk_he.Wunderlist.domain.mapper.CustomUserMapper;
import com.mk_he.Wunderlist.domain.request.AuthenticationRequest;
import com.mk_he.Wunderlist.domain.request.RegisterUserRequest;
import com.mk_he.Wunderlist.domain.response.AuthenticationResponse;
import com.mk_he.Wunderlist.domain.response.ResponseDto;
import com.mk_he.Wunderlist.exception.PasswordContextInvalidException;
import com.mk_he.Wunderlist.exception.PasswordLengthInvalidException;
import com.mk_he.Wunderlist.exception.UserAlreadyExistsException;
import com.mk_he.Wunderlist.exception.UsernameLengthInvalidException;
import com.mk_he.Wunderlist.repository.UserRepository;
import com.mk_he.Wunderlist.service.AuthService;
import com.mk_he.Wunderlist.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final ValidationService validationService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, ValidationService validationService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.validationService = validationService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseDto<String> registerUser(RegisterUserRequest request) {
        String message = "User registered successfully";

        try {
            validationService.validateUsername(request.getUsername());
            validationService.validatePassword(request.getPassword());

            userRepository.findByUsername(request.getUsername())
                    .ifPresentOrElse(
                            user -> {
                                throw new UserAlreadyExistsException(user.getUsername());
                            },
                            () -> {
                                String encodedPassword = passwordEncoder.encode(request.getPassword());
                                request.setPassword(encodedPassword);
                                User user = CustomUserMapper.registerAuthRequestToUser(request);
                                userRepository.save(user);
                            }
                    );
            return ResponseDto.success(message, request.getUsername());
        } catch (UserAlreadyExistsException e) {
            logger.error("User registration failed: Username '{}' already exists", request.getUsername(), e);
            return ResponseDto.failure("User already exists with username: " + request.getUsername(), null);
        } catch (UsernameLengthInvalidException e) {
            String errorMessage = UsernameLengthInvalidException.ERROR_MESSAGE;
            logger.error("Validation error on username '{}' password '{}' while register", request.getUsername(), errorMessage);
            return ResponseDto.failure(errorMessage, null);
        } catch (PasswordLengthInvalidException | PasswordContextInvalidException e) {
            logger.error("Validation error on username '{}' password '{}' while register", request.getUsername(), e.getMessage());
            return ResponseDto.failure("Password should contain at least 1 lower and 1 upper case letter and Password length should be grater than 8 and lower then 20. ", null);
        } catch (Exception e) {
            logger.error("User registration failed: Unexpected error for username '{}'", request.getUsername(), e);
            return ResponseDto.failure("Unexpected error occurred: " + e.getMessage(), null);
        }
    }

    @Override
    public ResponseDto<String> authenticateUser(AuthenticationRequest request) {
        String message = "User authenticated successfully";
        AuthenticationResponse response = null;

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtUtil.GenerateToken(request.getUsername());
                return ResponseDto.success(message, token);
            } else {
                message = "Authentication failed";
                return ResponseDto.failure(message, message);
            }
        } catch (UsernameNotFoundException e) {
            logger.error("User login failed. UsernameNotFoundException username - '{}'", request.getUsername(), e);
            message = "Username not found";
            return ResponseDto.failure(message, null);
        } catch (AuthenticationException e) {
            logger.error("User authentication failed. AuthenticationException username - '{}'", request.getUsername(), e);
            message = "Invalid credentials";
            return ResponseDto.failure(message, null);
        }
    }
}

