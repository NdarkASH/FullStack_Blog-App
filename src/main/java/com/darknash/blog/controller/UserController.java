package com.darknash.blog.controller;

import com.darknash.blog.constant.ApiPaths;
import com.darknash.blog.dto.AppResponse;
import com.darknash.blog.dto.CreateNewUser;
import com.darknash.blog.dto.NewUserResponse;
import com.darknash.blog.mapper.UserMapper;
import com.darknash.blog.model.User;
import com.darknash.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = ApiPaths.REGISTER)
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppResponse<NewUserResponse> createdNewUser(@Valid @RequestBody CreateNewUser request) {
        User newUser = userService.createNewUser(request);
        NewUserResponse newUserResponse = userMapper.toUserResponse(newUser);
        return AppResponse.<NewUserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .msg(HttpStatus.CREATED.getReasonPhrase())
                .data(newUserResponse)
                .build();
    }



}
