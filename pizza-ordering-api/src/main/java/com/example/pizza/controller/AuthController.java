package com.example.pizza.controller;

import com.example.pizza.dto.AuthRequest;
import com.example.pizza.dto.AuthResponse;
import com.example.pizza.entity.User;
import com.example.pizza.security.JwtProvider;
import com.example.pizza.security.UserPrincipal;
import com.example.pizza.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/signup")
    public AuthResponse signUp(@RequestBody @Valid AuthRequest authRequest, @RequestParam String email) {
        // Register new user
        User newUser = userService.registerUser(authRequest.getUsername(), email, authRequest.getPassword());
        // Generate token
        String token = jwtProvider.generateToken(newUser.getUsername());
        return new AuthResponse(token);
    }

    @PostMapping("/signin")
    public AuthResponse signIn(@RequestBody @Valid AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtProvider.generateToken(userPrincipal.getUsername());
        return new AuthResponse(token);
    }
}
