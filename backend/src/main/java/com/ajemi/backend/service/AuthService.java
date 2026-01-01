package com.ajemi.backend.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ajemi.backend.entity.Role;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.exception.ApiException;
import com.ajemi.backend.repository.RoleRepository;
import com.ajemi.backend.repository.UserRepository;
import com.ajemi.backend.security.JwtService;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, 
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
    }

    // 🔹 Register avec token incluant role
    public String register(String username, String email, String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new ApiException("Password cannot be empty");
        }
        if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
            throw new ApiException("User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));

        Role userRole = roleRepository.findByName(Role.RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Role USER non trouvé"));
        user.setRole(userRole); // set role USER par défaut

        userRepository.save(user);

        // token avec role
        return jwtService.generateTokenWithRoles(username, user.getRole());
    }

    // 🔹 Login
    public String login(String usernameOrEmail, String rawPassword) {
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new ApiException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new ApiException("Invalid credentials");
        }
        if(user.isBanned()){
            throw new ApiException("you are banned");
        }
        

        return jwtService.generateTokenWithRoles(user.getUsername(), user.getRole());
    }
}


