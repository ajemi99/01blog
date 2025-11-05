package com.ajemi.backend.controller;

import com.ajemi.backend.dto.AuthResponseDTO;
import com.ajemi.backend.dto.LoginRequestDTO;
import com.ajemi.backend.dto.RegisterRequestDTO;
import com.ajemi.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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
    public ResponseEntity<?> me(Authentication authentication) {
        return ResponseEntity.ok(Map.of("username", authentication.getName()));
    }
}
