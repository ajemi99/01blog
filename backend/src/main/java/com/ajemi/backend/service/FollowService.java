package com.ajemi.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ajemi.backend.repository.FollowRepository;
import com.ajemi.backend.repository.UserRepository;
import com.ajemi.backend.entity.User;
import lombok.RequiredArgsConstructor;
import com.ajemi.backend.entity.Subscription;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public void follow(Long followerId, Long followingId) {
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
        User following = userRepository.findById(followingId).orElseThrow();

        Subscription follow = new Subscription();
        follow.setFollower(follower);
        follow.setFollowing(following);

        followRepository.save(follow);

    }

    public void unfollow(Long followerId, Long followingId){
        followRepository.deleteByFollowerIdAndFollowingId(followerId,followingId);
    }
}
