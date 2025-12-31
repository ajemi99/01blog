package com.ajemi.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ajemi.backend.entity.Notification.NotificationType;
import com.ajemi.backend.entity.Subscription;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.FollowRepository;
import com.ajemi.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public void follow(@NonNull Long followerId,@NonNull Long followingId) {
        if (followerId.equals(followingId)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "You cannot follow yourself"
            );
        }
        
        boolean exists = followRepository
        .existsByFollowerIdAndFollowingId(followerId, followingId);
        if (exists) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Already following this user"
            );
        }
        
        User follower = userRepository.findById(followerId).orElseThrow();
        User following = userRepository.findById( followingId).orElseThrow();
        
        Subscription follow = new Subscription();
        follow.setFollower(follower);
        follow.setFollowing(following);
        
        followRepository.save(follow);
        // 🔔 CREATE NOTIFICATION
        notificationService.createNotification(
            following,   // receiver (لي تبعوه)
            follower,    // actor (لي دار follow)
            NotificationType.FOLLOW
        );
    }
    @Transactional
    public void unfollow(Long followerId, Long followingId){
        followRepository.deleteByFollowerIdAndFollowingId(followerId,followingId);
    }
}
