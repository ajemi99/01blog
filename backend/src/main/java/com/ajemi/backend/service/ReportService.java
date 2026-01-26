package com.ajemi.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ajemi.backend.dto.AdminReportResponseDTO;
import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.Report;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.exception.ApiException;
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
                .orElseThrow(() -> new ApiException("Reporter not found", HttpStatus.NOT_FOUND));
        Post post = postRepository.findById(postId)
               .orElseThrow(() -> new ApiException("Post not found", HttpStatus.NOT_FOUND));
        if (reporter.isBanned()) {
            throw new ApiException("Your account is suspended. You cannot report posts.", HttpStatus.FORBIDDEN);
        }
        // 🛑 ما يمكنش report راسك
        if (reporter.getId().equals(post.getAuthor().getId())) {
                throw new ApiException("You cannot report your own post.", HttpStatus.BAD_REQUEST);
        }

        Report report = new Report();
        report.setReporter(reporter);
        report.setReportedUser(post.getAuthor());
        report.setReason(reason);
        report.setPost(post);


        reportRepository.save(report);
    }
    
   public Page<AdminReportResponseDTO> getAllReports(int page, int size) {
          int validatedSize = (size > 50) ? 10 : size;
        Pageable pageable = PageRequest.of(page, validatedSize, Sort.by("createdAt").descending());
    return reportRepository.findAll(pageable)

            .map(r -> new AdminReportResponseDTO(
                    r.getId(),
                    r.getReason(),
                    r.getReporter().getUsername(),
                    r.getReportedUser().getUsername(),
                    r.getReportedUser().getRole().getName().name(),
                    r.getPost() != null ? r.getPost().getId() : null,
                    r.getCreatedAt()
            ))
            ;
    }

}
