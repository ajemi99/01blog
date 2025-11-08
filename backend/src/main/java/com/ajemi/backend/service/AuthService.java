package com.ajemi.backend.service;

import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.UserRepository;
import com.ajemi.backend.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ajemi.backend.entity.Role;
import com.ajemi.backend.exception.ApiException;
import com.ajemi.backend.repository.RoleRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
    }

    public String register(String username, String email, String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new ApiException("Password cannot be empty");
        }
        if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
            throw new ApiException("User already exists");
        }
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        Role userRole = roleRepository.findByName(Role.RoleName.USER)
            .orElseThrow(() -> new RuntimeException("Role USER non trouvé"));
        u.setRole(userRole);
        userRepository.save(u);
        return jwtService.generateToken(username);
    }

    public String login(String usernameOrEmail, String rawPassword) {
        User u = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new ApiException("User not found"));
        if (!passwordEncoder.matches(rawPassword, u.getPassword())) {
            throw new ApiException("Invalid credentials");
        }
        return jwtService.generateToken(u.getUsername());
    }
}


