package com.mk_he.Wunderlist.service;

import com.mk_he.Wunderlist.domain.request.AuthenticationRequest;
import com.mk_he.Wunderlist.domain.request.RegisterUserRequest;
import com.mk_he.Wunderlist.domain.response.ResponseDto;

public interface AuthService {

    ResponseDto<String> registerUser(RegisterUserRequest request);

    //    ResponseDto<AuthenticationResponse> authenticateUser(AuthenticationRequest request);
    ResponseDto<String> authenticateUser(AuthenticationRequest request);
}
