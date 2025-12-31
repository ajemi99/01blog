package com.ajemi.backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ajemi.backend.dto.AuthResponseDTO;
import com.ajemi.backend.dto.LoginRequestDTO;
import com.ajemi.backend.dto.RegisterRequestDTO;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.service.AuthService;
import com.ajemi.backend.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService,UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO body) {
        String token = authService.register(body.username(), body.email(), body.password());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO body) {
        String token = authService.login(body.usernameOrEmail(), body.password());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

   @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    User user = userService.findByUsername(authentication.getName());
    return ResponseEntity.ok(Map.of(
        "id", user.getId(),
        "username", user.getUsername()
    ));
}
}
