package com.ajemi.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajemi.backend.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // نجيبو جميع التعليقات ديال post واحد
    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);
}