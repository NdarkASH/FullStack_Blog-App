package com.darknash.blog.service;

import com.darknash.blog.dto.CreateNewUser;
import com.darknash.blog.model.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID uuid);
    User createNewUser(CreateNewUser request);
}
