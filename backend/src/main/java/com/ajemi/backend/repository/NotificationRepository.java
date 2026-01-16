package com.ajemi.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ajemi.backend.entity.Notification;
import com.ajemi.backend.entity.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    void deleteAllByActor(User actor);
    void deleteAllByUser(User user);
    Long countByUserIdAndReadFalse(Long userId);
    Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    List<Notification> findByUserAndReadFalse(User user);
}

