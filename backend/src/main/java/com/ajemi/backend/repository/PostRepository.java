package com.ajemi.backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    int countByAuthor_Id(Long id);
    // 🚀 Had l-query hiya l-feks dial l-Performance
    // Kat-jbed ghir l-IDs dial l-posts li liked mn taraf l-user wast l-feed
    @Query("SELECT l.post.id FROM Like l WHERE l.user.id = :userId AND l.post.id IN :postIds")
    List<Long> findLikedPostIds(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);
}


