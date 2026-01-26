package com.ajemi.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ajemi.backend.entity.Comment;
import com.ajemi.backend.entity.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // نجيبو جميع التعليقات ديال post واحد
    Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId,Pageable pageable);
    void deleteAllByUser(User user);
}
