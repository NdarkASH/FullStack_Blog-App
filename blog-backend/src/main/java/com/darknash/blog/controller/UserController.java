package com.darknash.blog.controller;

import com.darknash.blog.constant.ApiPaths;
import com.darknash.blog.dto.AppResponse;
import com.darknash.blog.dto.AuthResponse;
import com.darknash.blog.dto.CreateNewUser;
import com.darknash.blog.dto.NewUserResponse;
import com.darknash.blog.mapper.UserMapper;
import com.darknash.blog.model.User;
import com.darknash.blog.security.BlogUserDetails;
import com.darknash.blog.service.AuthenticationService;
import com.darknash.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = ApiPaths.REGISTER)
public class UserController {
    private final UserService userService;
//    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppResponse<AuthResponse> createdNewUser(@Valid @RequestBody CreateNewUser request) {
        User newUser = userService.createNewUser(request);
        UserDetails userDetails = new BlogUserDetails(newUser);
        String tokenValue = authenticationService.generateToken(userDetails);

        AuthResponse authResponse = AuthResponse.builder()
                .expiresIn(36000)
                .token(tokenValue)
                .build();

        return AppResponse.<AuthResponse>builder()
                .msg("New user created")
                .code(HttpStatus.CREATED.value())
                .data(authResponse)
                .build();
    }



}
