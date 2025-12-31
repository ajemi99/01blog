package com.ajemi.backend.controller;

import java.util.List;

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
import lombok.RequiredArgsConstructor;

 @RestController
 @RequestMapping("/api/admin")
 @RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final ReportService reportService;
    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminReportResponseDTO>getAllReports() {
        return reportService.getAllReports();
    }
    // ---------------- Users ----------------
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

     @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    //---------------------posts----------------------------
        @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        return ResponseEntity.ok(adminService.getAllPosts());
    }
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        adminService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reports/{id}/action")
    public ResponseEntity<Void> takeActionOnReport(@PathVariable Long id, @RequestParam String action) {
        adminService.handleReport(id, action);
        return ResponseEntity.ok().build();
    }
}
