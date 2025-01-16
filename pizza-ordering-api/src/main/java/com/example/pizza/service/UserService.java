package com.example.pizza.service;

import com.example.pizza.entity.Role;
import com.example.pizza.entity.User;
import com.example.pizza.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String email, String rawPassword) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already in use.");
        }

        User newUser = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(Role.CUSTOMER)  // default role is CUSTOMER
                .build();

        return userRepository.save(newUser);
    }
}

