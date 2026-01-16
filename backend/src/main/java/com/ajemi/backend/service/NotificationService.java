package com.ajemi.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajemi.backend.dto.NotificationResponseDTO;
import com.ajemi.backend.entity.Notification;
import com.ajemi.backend.entity.Notification.NotificationType;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

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
// NotificationService.java
public Page<NotificationResponseDTO> getUserNotifications(User user, int page, int size) {
    // 1. Cree Pageable (page hiya r-rqem, size hiya ch-hal mn wa7da f saf7a)
    Pageable pageable = PageRequest.of(page, size);
    
    // 2. Fetch data mn Repo
    Page<Notification> notificationsPage = notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable);

    // 3. Map l-entities l DTOs wast l-Page nichan
    return notificationsPage.map(n -> new NotificationResponseDTO(
            n.getId(),
            buildMessage(n),
            n.isRead(),
            n.getCreatedAt()
    ));
}

        // ✅ علّم notification كمقروءة
    @Transactional
    public void markAsRead(@NonNull Long notificationId, User user) {

        Notification notification = notificationRepository.findById( notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        // 🛑 ماشي ديالك
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Forbidden");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }
    public String buildMessage(Notification notification) {
      return switch (notification.getType()) {
          case FOLLOW -> notification.getActor().getUsername() + " started following you";
          case LIKE -> notification.getActor().getUsername() + " liked your post";
          case COMMENT -> notification.getActor().getUsername() + " commented on your post";
          case POST -> notification.getActor().getUsername() + " published a new post: ";
         default -> "You have a new notification";
      };
    }

    public Long getUnreadCount(Long receiverId){
        return notificationRepository.countByUserIdAndReadFalse(receiverId);
    }
    @Transactional
        public void markAllAsRead(User user) {
            // 1. Jbed ga3 li ma-m9riyinch
            List<Notification> unreadNotifications = notificationRepository.findByUserAndReadFalse(user);
            
            // 2. Raddhom kamlin true
            unreadNotifications.forEach(n -> n.setRead(true));
            
            // 3. Save
            notificationRepository.saveAll(unreadNotifications);
        }
}
