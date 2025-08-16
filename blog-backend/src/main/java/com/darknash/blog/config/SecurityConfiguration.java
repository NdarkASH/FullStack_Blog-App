package com.darknash.blog.config;

import com.darknash.blog.model.Category;
import com.darknash.blog.model.Tag;
import com.darknash.blog.model.User;
import com.darknash.blog.repository.CategoryRepository;
import com.darknash.blog.repository.TagRepository;
import com.darknash.blog.repository.UserRepository;
import com.darknash.blog.security.BlogUserDetailsService;
import com.darknash.blog.security.JwtAuthenticationFilter;
import com.darknash.blog.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public AuthenticationManager authManagerConfig(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationService authenticationService) throws Exception {
        return new JwtAuthenticationFilter(authenticationService);
    }


    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        BlogUserDetailsService userDetailsService = new BlogUserDetailsService(userRepository);

        String email = "Darknash@gmail.com";
        userRepository.findByEmail(email)
                .orElseGet(()-> {
                    User user = new User();
                    user.setEmail(email);
                    user.setFirstName("Darknash");
                    user.setLastName("Naofumi");
                    user.setPassword(passwordEncoder().encode("Darknash"));
                    return userRepository.save(user);
                });
        return userDetailsService;
    }

    @Bean
    public CommandLineRunner loadTagData(TagRepository tagRepository) {
        return args -> {
            if (tagRepository.count() == 0) {
                Tag tag1 = new Tag();
                tag1.setName("Darknash");
                tagRepository.save(tag1);
                Tag tag2 = new Tag();
                tag2.setName("Naofumi");
                tagRepository.save(tag2);
            }
        };
    }

    @Bean
    public CommandLineRunner loadCategory(CategoryRepository categoryRepository) {
        return args -> {
            if (categoryRepository.count() == 0) {
                Category category1 = new Category();
                category1.setName("Darknash");
                categoryRepository.save(category1);
                Category category2 = new Category();
                category2.setName("Naofumi");
                categoryRepository.save(category2);
            }
        };
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            JwtAuthenticationFilter jwtAuthenticationFilter
    )throws Exception {
        httpSecurity.authorizeHttpRequests(
                authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/drafts").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tags/**").authenticated().anyRequest().authenticated()
        ).csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
