package com.ajemi.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ajemi.backend.dto.CommentRequestDTO;
import com.ajemi.backend.dto.CommentResponseDTO;
import com.ajemi.backend.entity.Comment;
import com.ajemi.backend.entity.Notification.NotificationType;
import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.exception.ApiException;
import com.ajemi.backend.repository.CommentRepository;
import com.ajemi.backend.repository.PostRepository;
import com.ajemi.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
     private final NotificationService notificationService;

    public CommentResponseDTO addComment(@NonNull Long userId, CommentRequestDTO request) {
        
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ApiException("Post not found", HttpStatus.NOT_FOUND));
        User actor = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        // 🚩 Check wach l-user banni
        if (actor.isBanned()) {
            throw new ApiException("Your account is suspended. You cannot comment.", HttpStatus.FORBIDDEN);
        }

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(actor);
        comment.setContent(request.getContent());
        
        Comment saved = commentRepository.save(comment);

        User receiver = post.getAuthor();
        
             notificationService.createNotification(
            receiver,
            actor,
            NotificationType.COMMENT
        );

        return mapToDTO(saved);
    }

    public List<CommentResponseDTO> getCommentsByPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private CommentResponseDTO mapToDTO(Comment comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUsername(comment.getUser().getUsername());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
public void deleteComment( @NonNull Long commentId, Long userId) {
    Comment comment = commentRepository.findById( commentId)
            .orElseThrow(() -> new ApiException("Comment not found", HttpStatus.NOT_FOUND));

        // 🚩 Permissions logic:
        // 1. Mol l-comment i-qder i-ms7ou
        // 2. Mol l-post i-qder i-msa7 ay comment f l-post dyalo
        boolean isCommentOwner = comment.getUser().getId().equals(userId);
        boolean isPostOwner = comment.getPost().getAuthor().getId().equals(userId);
        if (!isCommentOwner && !isPostOwner) {
                    throw new ApiException("You don't have permission to delete this comment", HttpStatus.FORBIDDEN);
         }
        commentRepository.delete(comment);
   
    }

}

