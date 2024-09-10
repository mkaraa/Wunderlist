package com.mk_he.Wunderlist.service.impl;

import com.mk_he.Wunderlist.domain.entity.User;
import com.mk_he.Wunderlist.domain.response.ResponseDto;
import com.mk_he.Wunderlist.exception.UserNotFoundException;
import com.mk_he.Wunderlist.repository.UserRepository;
import com.mk_he.Wunderlist.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseDto<String> getUserByUsername(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("Username not found: " + username));
            return ResponseDto.success("Username exists", user.getUsername());
        } catch (UserNotFoundException e) {
            logger.error("Username not found with username: {}", username);
            return ResponseDto.failure("Username not found", username);
        }
    }

}
