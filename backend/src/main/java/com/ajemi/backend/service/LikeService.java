package com.ajemi.backend.service;

import org.springframework.stereotype.Service;

import com.ajemi.backend.entity.Like;
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

    public String toggleLike(Long postId, String username) {

        // 1) نجيبو البوست
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 2) نجيبو user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3) نشوفو واش دار like قبل
        var existing = likeRepository.findByPostAndUser(post, user);

        if (existing.isPresent()) {
            // --- كان دير like → نديرو UNLIKE
            likeRepository.delete(existing.get());
            return "unliked";
        }

        // --- ما دارش like → نديرو LIKE
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);

        return "liked";
    }

    public int getLikesCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return likeRepository.countByPost(post);
    }
}
