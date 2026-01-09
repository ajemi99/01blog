package com.ajemi.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajemi.backend.dto.AuthResponseDTO;
import com.ajemi.backend.dto.InfoDto;
import com.ajemi.backend.dto.LoginRequestDTO;
import com.ajemi.backend.dto.RegisterRequestDTO;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.FollowRepository;
import com.ajemi.backend.security.UserDetailsImpl;
import com.ajemi.backend.service.AuthService;
import com.ajemi.backend.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final FollowRepository followRepository;

    public AuthController(AuthService authService,UserService userService,FollowRepository followRepository) {
        this.authService = authService;
        this.userService = userService;
        this.followRepository = followRepository;
    }

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

   @GetMapping("/me")
    public ResponseEntity<InfoDto> getCurrentUser( @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
    User user = userService.findUser(userId);
        Long followersCount = followRepository.countFollowers(userId);
        Long followingCount = followRepository.countFollowing(userId);
        return ResponseEntity.ok(new InfoDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                followingCount,
                followersCount
        ));
}
}
