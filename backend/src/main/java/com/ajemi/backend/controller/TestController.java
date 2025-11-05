package com.ajemi.backend.controller;

// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// @CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public String testConnection() {
        return "Connexion réussie ! ✅";
    }
}