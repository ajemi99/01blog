package com.ajemi.backend.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.ajemi.backend.dto.CommentRequestDTO;
import com.ajemi.backend.dto.CommentResponseDTO;
import com.ajemi.backend.entity.Comment;
import com.ajemi.backend.entity.Notification.NotificationType;
import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.User;
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

    public CommentResponseDTO addComment(Long userId, CommentRequestDTO request) {
        
        Post post = postRepository.findById(@NonNull request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User actor = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User receiver = post.getAuthor();
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(actor);
        comment.setContent(request.getContent());

        Comment saved = commentRepository.save(comment);
        if (actor.getId().equals(receiver.getId())){
            return mapToDTO(saved); 
        }
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
public void deleteComment(Long commentId, Long userId) {
    Comment comment = commentRepository.findById(@NonNull commentId)
            .orElseThrow(() -> new RuntimeException("التعليق غير موجود"));

    if (comment.getUser() == null) {
        throw new RuntimeException("التعليق ليس له مستخدم مرتبط");
    }

    if (!comment.getUser().getId().equals(userId)) {
        throw new RuntimeException("ليس لديك الحق في حذف هذا التعليق");
    }

    try {
        commentRepository.delete(comment);
    } catch (Exception e) {
        throw new RuntimeException("حدث خطأ أثناء الحذف: " + e.getMessage());
    }
}

}

