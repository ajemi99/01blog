package com.ajemi.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajemi.backend.entity.Like;
import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.User;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndUser(Post post, User user);
    boolean existsByPostIdAndUserUsername(Long postId, String username);
    int countByPost(Post post);
    void deleteAllByUser(User user);
}
