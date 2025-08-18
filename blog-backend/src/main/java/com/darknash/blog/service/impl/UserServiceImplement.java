package com.darknash.blog.service.impl;

import com.darknash.blog.dto.CreateNewUser;
import com.darknash.blog.exception.DuplicateEntity;
import com.darknash.blog.model.User;
import com.darknash.blog.repository.UserRepository;
import com.darknash.blog.service.AuthenticationService;
import com.darknash.blog.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @Override
    public User getUserById(UUID uuid) {
        return userRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + uuid));
    }

    @Override
    public User createNewUser(CreateNewUser request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEntity("Email is already in use");
        }

        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(newUser);
    }
}
