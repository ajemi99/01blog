package com.ajemi.backend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.ajemi.backend.security.UserDetailsImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajemi.backend.dto.AdminReportResponseDTO;
import com.ajemi.backend.dto.PostResponseDTO;
import com.ajemi.backend.service.PostService;
import com.ajemi.backend.service.ReportService;
import lombok.RequiredArgsConstructor;
 @RestController
 @RequestMapping("/api/admin")
 @RequiredArgsConstructor
public class AdminController {
    private final PostService postService;
    private final ReportService reportService;
        @GetMapping("/posts")
    public List<PostResponseDTO> getAllPostsForAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    
    String username = userDetails.getUsername();
    return postService.getAllPostsForAdmin(username);
    }
    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminReportResponseDTO>getAllReports() {
        return reportService.getAllReports();
    }

}
