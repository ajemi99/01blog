package com.ajemi.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajemi.backend.dto.AuthResponseDTO;
import com.ajemi.backend.dto.LoginRequestDTO;
import com.ajemi.backend.dto.RegisterRequestDTO;
import com.ajemi.backend.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO body) {
        String token = authService.register(body.getUsername(), body.getEmail(), body.getPassword());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO body) {
        String token = authService.login(body.identifier(), body.password());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

}
