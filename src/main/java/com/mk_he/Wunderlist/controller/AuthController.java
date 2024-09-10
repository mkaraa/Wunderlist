package com.mk_he.Wunderlist.controller;

import com.mk_he.Wunderlist.domain.request.AuthenticationRequest;
import com.mk_he.Wunderlist.domain.request.RegisterUserRequest;
import com.mk_he.Wunderlist.domain.response.ResponseDto;
import com.mk_he.Wunderlist.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseDto<String> registerUser(@RequestBody RegisterUserRequest request) {
        return authService.registerUser(request);
    }

    @PostMapping("login")
    public ResponseDto<String> authenticateUser(@RequestBody AuthenticationRequest request) {
        return authService.authenticateUser(request);
    }
}
