package com.mk_he.Wunderlist.service;

import com.mk_he.Wunderlist.domain.entity.User;
import com.mk_he.Wunderlist.domain.response.ResponseDto;
import com.mk_he.Wunderlist.repository.UserRepository;
import com.mk_he.Wunderlist.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void getUserByUsername_Success() {
        String username = "mkaraa-user";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));


        ResponseDto<String> responseDto = userService.getUserByUsername(username);

        assertNotNull(responseDto);
        assertTrue(responseDto.isSuccess());
        assertEquals(username, responseDto.getData());
    }

    @Test
    public void getUserByUsername_NotFound() {
        String username = "nonexists_mkaraa";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ResponseDto<String> responseDto = userService.getUserByUsername(username);

        assertNotNull(responseDto);
        assertFalse(responseDto.isSuccess());
        assertEquals(username, responseDto.getData());
    }
}
