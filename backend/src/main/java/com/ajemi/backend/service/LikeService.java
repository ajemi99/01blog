package com.ajemi.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ajemi.backend.entity.Like;
import com.ajemi.backend.entity.Notification.NotificationType;
import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.exception.ApiException;
import com.ajemi.backend.repository.LikeRepository;
import com.ajemi.backend.repository.PostRepository;
import com.ajemi.backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
@Transactional
    public String toggleLike(@NonNull Long postId, String username) {

        // 1) نجيبو البوست
        Post post = postRepository.findById(postId)
               .orElseThrow(() -> new ApiException("Post not found", HttpStatus.NOT_FOUND));
    // 2️⃣ actor (لي دار like)
        User actor = userRepository.findByUsername(username)
           .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

        if (actor.isBanned()) {
            throw new ApiException("Your account is suspended.", HttpStatus.FORBIDDEN);
        }
        User receiver = post.getAuthor();
        // 3) نشوفو واش دار like قبل
       var existing = likeRepository.findByPostIdAndUserId(postId, actor.getId());

        if (existing.isPresent()) {
            // --- كان دير like → نديرو UNLIKE
            likeRepository.delete(existing.get());
            return "unliked";
        }

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

    // public int getLikesCount(@NonNull Long postId) {

    //     return likeRepository.countByPostId(postId);
    // }
}
