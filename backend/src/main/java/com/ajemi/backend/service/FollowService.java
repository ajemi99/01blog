package com.ajemi.backend.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajemi.backend.entity.Notification.NotificationType;
import com.ajemi.backend.entity.Subscription;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.exception.ApiException;
import com.ajemi.backend.repository.FollowRepository;
import com.ajemi.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
  @Transactional
public boolean toggleFollow(Long followerId, Long followingId) {
    if (followerId.equals(followingId)) {
       throw new ApiException("You cannot follow yourself", HttpStatus.BAD_REQUEST);
    }
    User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ApiException("Follower not found", HttpStatus.NOT_FOUND));
        
     User following = userRepository.findById(followingId)
                .orElseThrow(() -> new ApiException("User to follow not found", HttpStatus.NOT_FOUND));

        // 🚩 Check: Wach ana banni?
        if (follower.isBanned()) {
            throw new ApiException("Your account is suspended. You cannot follow users.", HttpStatus.FORBIDDEN);
        }
    Optional<Subscription> existingFollow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);

    if (existingFollow.isPresent()) {
        // Ila kan aslan, n-ms7ouh (Unfollow)
        followRepository.delete(existingFollow.get());
        return false;
    } else {

        Subscription follow = new Subscription();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);       
        notificationService.createNotification(following, follower, NotificationType.FOLLOW);
        return true;
    }
}
}
