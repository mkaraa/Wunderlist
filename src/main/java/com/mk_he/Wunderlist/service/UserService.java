package com.mk_he.Wunderlist.service;

import com.mk_he.Wunderlist.domain.response.ResponseDto;

public interface UserService {
    ResponseDto<String> getUserByUsername(String username);
}
