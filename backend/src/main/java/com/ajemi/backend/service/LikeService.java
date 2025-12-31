package com.ajemi.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;
import com.ajemi.backend.entity.Like;
import com.ajemi.backend.entity.Notification.NotificationType;
import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.LikeRepository;
import com.ajemi.backend.repository.PostRepository;
import com.ajemi.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public String toggleLike(Long postId, String username) {

        // 1) نجيبو البوست
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

    // 2️⃣ actor (لي دار like)
        User actor = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        User receiver = post.getAuthor();
        // 3) نشوفو واش دار like قبل
        var existing = likeRepository.findByPostAndUser(post, actor);

        if (existing.isPresent()) {
            // --- كان دير like → نديرو UNLIKE
            likeRepository.delete(@NonNull existing.get());
            return "unliked";
        }
        if (actor.getId().equals(receiver.getId())) {
            return "liked"; // أو return بلا notification
        }
        // --- ما دارش like → نديرو LIKE
        Like like = new Like();
        like.setPost(post);
        like.setUser(actor);
        likeRepository.save(like);

            notificationService.createNotification(
            receiver,
            actor,
            NotificationType.LIKE
        );
        return "liked";
    }

    public int getLikesCount(Long postId) {
        Post post = postRepository.findById(@NonNull postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return likeRepository.countByPost(post);
    }
}
