package com.darknash.blog.service;

import com.darknash.blog.model.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID uuid);
}
