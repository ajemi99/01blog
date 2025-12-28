package com.ajemi.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ajemi.backend.service.UserService;
import java.util.List;
import com.ajemi.backend.dto.UserSearchDTO;



@RestController
@RequestMapping("/api/users")

public class UserController{

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/search")
    public List<UserSearchDTO> search( @RequestParam String s, @RequestParam Long currentUserId){
        return userService.searchUsers(s,currentUserId);
    }
    
}

