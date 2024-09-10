package com.mk_he.Wunderlist.domain.mapper;

import com.mk_he.Wunderlist.domain.entity.User;
import com.mk_he.Wunderlist.domain.request.RegisterUserRequest;

public class CustomUserMapper {
    public static User registerAuthRequestToUser(RegisterUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        return user;
    }
}
