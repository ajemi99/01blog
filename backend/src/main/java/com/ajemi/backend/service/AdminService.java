package com.ajemi.backend.service;

import java.util.List;

import com.ajemi.backend.dto.PostDTO;
import com.ajemi.backend.dto.UserDTO;
import com.ajemi.backend.entity.Report;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.UserRepository;
import com.ajemi.backend.repository.CommentRepository;
import com.ajemi.backend.repository.LikeRepository;
import com.ajemi.backend.repository.NotificationRepository;
import com.ajemi.backend.repository.PostRepository;


import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import com.ajemi.backend.repository.FollowRepository;
import com.ajemi.backend.repository.ReportRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReportRepository reportRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final FollowRepository  subscriptionRepository;
    private final NotificationRepository notificationRepository;

    // ---------------- USERS ----------------------------------------------------------------------
        public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::new)
                .toList();
        }
 @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1️⃣ Delete notifications where user is actor or owner
        notificationRepository.deleteAllByActor(user);
        notificationRepository.deleteAllByUser(user);

        // 2️⃣ Delete likes made by user
        likeRepository.deleteAllByUser(user);

        // 3️⃣ Delete comments made by user
        commentRepository.deleteAllByUser(user);

        // 4️⃣ Delete subscriptions (follower or followed)
        subscriptionRepository.deleteAllByFollowerOrFollowing(user, user);

        // 5️⃣ Delete reports where user is reporter or reported
        reportRepository.deleteAllByReporterOrReportedUser(user, user);

        // 6️⃣ Delete posts (cascade will remove comments and likes)
        postRepository.deleteAllByAuthor(user);

        // 7️⃣ Finally, delete user
        userRepository.delete(user);
    }
// ---------------- POSTS ---------------------------------------------------------------------------
        public List<PostDTO> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostDTO::new)
                .toList();
        }

         public void deletePost(Long postId) {
             postRepository.deleteById(postId);
        }

 // ---------------- REPORTS ------------------------------------------------------------------------
        public void handleReport(Long reportId, String action) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        switch (action.toLowerCase()) {
            case "delete_post" -> {
                if (report.getPost() != null) {
                    postRepository.delete(report.getPost());
                }
            }
            case "ban_user" -> {
                User user = report.getReportedUser();
                user.setBanned(true); // خاصك boolean banned ف User
            }
            case "delete_user" -> {
                userRepository.delete(report.getReportedUser());
            }
            default -> throw new RuntimeException("Unknown action");
        }

        reportRepository.delete(report); // نحيدو report من بعد المعالجة
    }
    
}
