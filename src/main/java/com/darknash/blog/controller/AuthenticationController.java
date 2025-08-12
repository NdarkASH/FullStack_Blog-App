package com.darknash.blog.controller;

import com.darknash.blog.constant.ApiPaths;
import com.darknash.blog.dto.AppResponse;
import com.darknash.blog.dto.AuthResponse;
import com.darknash.blog.dto.LoginRequest;
import com.darknash.blog.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = ApiPaths.AUTH)
public class AuthenticationController {
    private final AuthenticationService authenticationService;



    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AppResponse<AuthResponse> login(@RequestBody LoginRequest request) {
        UserDetails userDetails = authenticationService.authenticate(
                request.getEmail(),
                request.getPassword()
        );
    String tokenValue = authenticationService.generateToken(userDetails);
    AuthResponse authResponse = AuthResponse.builder()
            .token(tokenValue)
            .expiresIn(86400)
            .build();

    return AppResponse.<AuthResponse>builder()
            .code(HttpStatus.ACCEPTED.value())
            .msg(HttpStatus.ACCEPTED.toString())
            .data(authResponse)
            .build();
    }

}
