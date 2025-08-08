package com.darknash.blog.config;

import com.darknash.blog.model.User;
import com.darknash.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserDetailsServiceConfig {

    private final PasswordEncoder passwordEncoder;


    public UserDetailsService userDetailsService(UserRepository userRepository) {
        BlogUserDetailsService userDetailsService = new BlogUserDetailsService(userRepository);

        String email = "darknash@gmail.com";
        userRepository.findByEmail(email)
                .orElseGet(()->{
                    User user = new User();
                    user.setEmail(email);
                    user.setFirstName("Darknash");
                    user.setLastName("Blog");
                    user.setPassword(passwordEncoder.encode("darknash"));
                    return userRepository.save(user);
                });
        return userDetailsService;
    }
}
