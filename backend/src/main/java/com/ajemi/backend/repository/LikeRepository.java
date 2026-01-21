package com.ajemi.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajemi.backend.entity.Like;
import com.ajemi.backend.entity.User;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);
    boolean existsByPostIdAndUserUsername(Long postId, String username);
    int countByPostId(Long postId);
    void deleteAllByUser(User user);
}
