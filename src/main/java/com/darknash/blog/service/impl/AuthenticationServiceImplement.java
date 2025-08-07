package com.darknash.blog.service.impl;

import com.darknash.blog.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplement implements AuthenticationService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDetails authenticate(String username, String password) {
        return null;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return "";
    }

    @Override
    public UserDetails validateToken(String token) {
        return null;
    }
}
