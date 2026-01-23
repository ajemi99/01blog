package com.ajemi.backend.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajemi.backend.dto.PostDTO;
import com.ajemi.backend.dto.UserDTO;
import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.Report;
import com.ajemi.backend.entity.Role;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.exception.ApiException;
import com.ajemi.backend.repository.CommentRepository;
import com.ajemi.backend.repository.FollowRepository;
import com.ajemi.backend.repository.LikeRepository;
import com.ajemi.backend.repository.NotificationRepository;
import com.ajemi.backend.repository.PostRepository;
import com.ajemi.backend.repository.ReportRepository;
import com.ajemi.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReportRepository reportRepository;
    private final FollowRepository  subscriptionRepository;
    private final NotificationRepository notificationRepository;
    private final FileStorageService fileStorageService;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    // ---------------- USERS ----------------------------------------------------------------------
        public List<UserDTO> getAllUsers() {
        return userRepository.findAllByRole_Name(Role.RoleName.USER)
                .stream()
                .map(UserDTO::new)
                .toList();
        }
 @Transactional
public void deleteUser(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

    // 🚩 Security Check
    if (user.getRole().getName().equals(Role.RoleName.ADMIN)) {
        throw new ApiException("Moussta7il t-msa7 Admin!", HttpStatus.FORBIDDEN);
    }

    // 1️⃣ Delete notifications (Actor & Receiver)
    notificationRepository.deleteAllByActor(user);
    notificationRepository.deleteAllByUser(user);
    notificationRepository.flush(); // 🔥 Force delete daba

    // 2️⃣ Delete likes & comments dyal had l-user
    likeRepository.deleteAllByUser(user);
    commentRepository.deleteAllByUser(user);
    likeRepository.flush();
    commentRepository.flush();

    // 3️⃣ Delete subscriptions (Followers/Following)
    subscriptionRepository.deleteAllByFollowerOrFollowing(user, user);
    subscriptionRepository.flush();

    // 4️⃣ Delete reports dyal had l-user
    reportRepository.deleteAllByReporterOrReportedUser(user, user);
    reportRepository.flush();

    // 5️⃣ Delete posts w d-data dyalhom
    List<Post> posts = postRepository.findAllByAuthor(user);
    for (Post post : posts) {
        // Msa7 l-file mn l-disk
        if (post.getMediaUrl() != null) {
            fileStorageService.deleteFile(post.getMediaUrl());
        }
        // 🚩 Darouri msa7 l-reports dial had l-post qbel ma t-msa7 l-post
        reportRepository.deleteByPostId(post.getId());
        postRepository.delete(post);
    }
    postRepository.flush(); // 🔥 Force delete posts

    // 6️⃣ Finally, delete l-user
    userRepository.delete(user);
}
// ---------------- POSTS ---------------------------------------------------------------------------
        public List<PostDTO> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostDTO::new)
                .toList();
        }
        @Transactional
         public void deletePost(Long postId) {
            Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("المنشور غير موجود بالمعرّف: " + postId));

         if (post.getMediaUrl() != null) {
             fileStorageService.deleteFile(post.getMediaUrl());
        }
        reportRepository.deleteByPostId(postId);
            postRepository.deleteById(postId);
        }

 // ---------------- REPORTS ------------------------------------------------------------------------
   @Transactional
public void handleReport(Long reportId, String action) {
    Report report = reportRepository.findById(reportId) 
           .orElseThrow(() -> new ApiException("Report not found", HttpStatus.NOT_FOUND));

    User reportedUser = report.getReportedUser();


    // 🚩 Logic d-protection: Ila l-reported user Admin, ma-ndiro walou f l-Ban/Delete
    boolean isTargetAdmin = reportedUser.getRole().getName().equals(Role.RoleName.ADMIN);

    switch (action.toLowerCase()) {
        case "delete_post" -> {
            if (report.getPost() != null) {
                deletePost(report.getPost().getId());
            }
        }
        case "ban_user" -> {
            if (isTargetAdmin) {
               throw new ApiException("Moussta7il t-banni Admin!", HttpStatus.FORBIDDEN);
            }
            reportedUser.setBanned(true);
        }
        case "delete_user" -> {
            if (isTargetAdmin) {
               if (isTargetAdmin) throw new ApiException("Moussta7il t-msa7 Admin!", HttpStatus.FORBIDDEN);
            }
            deleteUser(reportedUser.getId());
        }
        default -> throw new ApiException("Unknown action", HttpStatus.BAD_REQUEST);
    }

    reportRepository.delete(report); 
}
    //----------------------------------ban/unbann--------------------------------------------------
        @Transactional
        public void banUser(Long userId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // 🚩 Protection hna darouriya
            if (user.getRole().getName().equals(Role.RoleName.ADMIN)) {
                throw new RuntimeException("Impossible de bannir un administrateur");
            }

            user.setBanned(true);
            userRepository.save(user);
        }

    @Transactional
    public void unbanUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBanned(false);
     userRepository.save(user);
    }

}
