package com.ajemi.backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajemi.backend.dto.AdminReportResponseDTO;
import com.ajemi.backend.dto.PostDTO;
import com.ajemi.backend.dto.UserDTO;
import com.ajemi.backend.service.AdminService;
import com.ajemi.backend.service.ReportService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

 @RestController
 @RequestMapping("/api/admin")
 @RequiredArgsConstructor
 @PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final ReportService reportService;

    @GetMapping("/reports")
    public ResponseEntity<Page<AdminReportResponseDTO>>getAllReports(
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page cannot be negative") int page,
        @RequestParam(defaultValue = "10") @Max(value = 50, message = "Size too large") int size) {
       return ResponseEntity.ok(reportService.getAllReports(page,size));
    }
        @PostMapping("/reports/{id}/action")
    public ResponseEntity<Void> takeActionOnReport(@PathVariable Long id, @RequestParam String action) {
        adminService.handleReport(id, action);
        return ResponseEntity.ok().build();
    }
    // ---------------- Users ----------------
    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(  
           @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page cannot be negative") int page,
             @RequestParam(defaultValue = "10") @Max(value = 50, message = "Size too large") int size) {
        return ResponseEntity.ok(adminService.getAllUsers(page,size));
    }

     @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/users/{id}/ban")
    public ResponseEntity<Void> banUser(@PathVariable Long id) {
        adminService.banUser(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/users/{id}/unban")
    public ResponseEntity<Void> unbanUser(@PathVariable Long id) {
         adminService.unbanUser(id);
        return ResponseEntity.ok().build();
    }

    //---------------------posts----------------------------
        @GetMapping("/posts")
    public ResponseEntity<Page<PostDTO>> getAllPosts(           
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page cannot be negative") int page,
        @RequestParam(defaultValue = "10") @Max(value = 50, message = "Size too large") int size) {
        return ResponseEntity.ok(adminService.getAllPosts(page,size));
    }
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        adminService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
