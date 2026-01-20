package com.ajemi.backend.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ajemi.backend.dto.AdminReportResponseDTO;
import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.Report;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.PostRepository;
import com.ajemi.backend.repository.ReportRepository;
import com.ajemi.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

     public void reportPost(String reporterUsername, @NonNull Long postId, String reason) {

        User reporter = userRepository.findByUsername(reporterUsername)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Reported user not found"));

        // 🛑 ما يمكنش report راسك
        if (reporter.getId().equals(post.getAuthor().getId())) {
                throw new RuntimeException("You cannot report yourself");
        }

        Report report = new Report();
        report.setReporter(reporter);
        report.setReportedUser(post.getAuthor());
        report.setReason(reason);
        report.setPost(post);


        reportRepository.save(report);
    }
   public List<AdminReportResponseDTO> getAllReports() {
        
    return reportRepository.findAllByOrderByCreatedAtDesc()
            .stream()
            .map(r -> new AdminReportResponseDTO(
                    r.getId(),
                    r.getReason(),
                    r.getReporter().getUsername(),
                    r.getReportedUser().getUsername(),
                    r.getReportedUser().getRole().getName().name(),
                    r.getPost() != null ? r.getPost().getId() : null,
                    r.getCreatedAt()
            ))
            .toList();
    }

}
