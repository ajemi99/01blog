package com.ajemi.backend.service;

import com.ajemi.backend.entity.Notification.NotificationType;
import com.ajemi.backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ajemi.backend.dto.NotificationResponseDTO;
import com.ajemi.backend.entity.Notification;
import com.ajemi.backend.entity.User;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
  private final NotificationRepository notificationRepository;  
  public void createNotification(
            User receiver,   // لي غادي توصّلو
            User actor,      // لي دار الفعل
            NotificationType type
    ) {
        // 🛑 ما نديروش notification إلا كان user دارها مع راسو
        if (receiver.getId().equals(actor.getId())) {
            return;
        }

        Notification notification = new Notification();
        notification.setUser(receiver);
        notification.setActor(actor);
        notification.setType(type);
        notification.setRead(false);

        notificationRepository.save(notification);
    }

     // 📥 جيب notifications ديال user
    public List<NotificationResponseDTO> getUserNotifications(User user) {
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);

        // 🌟 تحويل كل Notification ل DTO
        return notifications.stream()
                .map(n -> new NotificationResponseDTO(
                        n.getId(),
                        buildMessage(n),
                        n.isRead(),
                        n.getCreatedAt()
                ))
                .toList();
    }

        // ✅ علّم notification كمقروءة
    @Transactional
    public void markAsRead(Long notificationId, User user) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        // 🛑 ماشي ديالك
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Forbidden");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }
    public String buildMessage(Notification notification) {
    switch(notification.getType()) {
        case FOLLOW:
            return notification.getActor().getUsername() + " started following you";
        case LIKE:
            return notification.getActor().getUsername() + " liked your post";
        case COMMENT:
            return notification.getActor().getUsername() + " commented on your post";
        default:
            return "You have a new notification";
    }
    }
}
