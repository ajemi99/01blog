package com.ajemi.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "notifications")
@Getter @Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👤 لي غادي توصّلو notification
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 👤 لي دار الفعل (follow / like / comment)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private User actor;

    // 🧾 نوع الإشعار
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
private NotificationType type;

    // 👁️ تقرات ولا لا
   @Column(name = "is_read", nullable = false) // Beddelna smiya f MySQL l- "is_read"
    private boolean read = false;

    // ⏰ وقت الإنشاء
    private LocalDateTime createdAt = LocalDateTime.now();
    
public enum NotificationType {
    FOLLOW,
    LIKE,
    COMMENT,
    POST,
}
}
