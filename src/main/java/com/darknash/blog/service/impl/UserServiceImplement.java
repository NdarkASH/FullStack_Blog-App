package com.darknash.blog.service.impl;

import com.darknash.blog.model.User;
import com.darknash.blog.repository.UserRepository;
import com.darknash.blog.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getUserById(UUID uuid) {
        return userRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + uuid));
    }
}
