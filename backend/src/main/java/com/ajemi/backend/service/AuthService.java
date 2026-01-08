package com.ajemi.backend.service;


import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ajemi.backend.entity.Role;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.exception.ApiException;
import com.ajemi.backend.repository.RoleRepository;
import com.ajemi.backend.repository.UserRepository;
import com.ajemi.backend.security.JwtService;
import org.springframework.transaction.annotation.Transactional;



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
    @Transactional
    public String register(String username, String email, String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
             throw new ApiException("Password cannot be empty",HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(email)) {
           throw new ApiException("Email already registered", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByUsername(username)){
            throw new ApiException("Username already taken", HttpStatus.BAD_REQUEST);
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
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new ApiException("Identifiants incorrects",HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
           throw new ApiException("Identifiants incorrects", HttpStatus.UNAUTHORIZED);
        }
        if(user.isBanned()){
            throw new ApiException("Votre compte est banni", HttpStatus.FORBIDDEN);
        }

        return jwtService.generateTokenWithRoles(user.getUsername(), user.getRole());
    }
}


