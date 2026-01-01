package com.ajemi.backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.User;

// import com.ajemi.backend.entity.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByAuthor_IdOrderByCreatedAtDesc(Long authorId);
    List<Post> findAllByAuthor_IdInOrderByCreatedAtDesc(List<Long> authorIds);
    void deleteAllByAuthor(User author);
    List<Post> findAllByAuthor(User author);
}


