package com.ajemi.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajemi.backend.dto.InfoDto;
import com.ajemi.backend.dto.UserProfileDTO;
import com.ajemi.backend.dto.UserSearchDTO;
import com.ajemi.backend.security.UserDetailsImpl;
import com.ajemi.backend.service.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController{

    private final UserService userService;


    @GetMapping("/search")
    public ResponseEntity<List<UserSearchDTO>> search( @RequestParam String query,@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<UserSearchDTO> results = userService.searchUsers(query, userDetails.getId());
        return ResponseEntity.ok(results);
    }
        @GetMapping("/profile/{username}")
    public ResponseEntity<UserProfileDTO> getProfile(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetailsImpl userDetails, // Jbed l-ID dyali men l-Token
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.getUserProfile(username, userDetails.getId(),page,size));
    }
       @GetMapping("/me")
    public ResponseEntity<InfoDto> getCurrentUser( @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        InfoDto infoDto = userService.getCurrentUser(userId);
        return ResponseEntity.ok(infoDto);
}
}

