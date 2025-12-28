package com.ajemi.backend.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ajemi.backend.dto.UserSearchDTO;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.UserRepository;
import com.ajemi.backend.repository.FollowRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

     public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<UserSearchDTO> searchUsers(String username, Long currentUserId){
        List<User> users = userRepository.findByUsernameContainingIgnoreCaseAndIdNot(username, currentUserId);
         return users.stream().map(user -> {
        UserSearchDTO dto = new UserSearchDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());

        boolean following = followRepository
            .existsByFollowerIdAndFollowingId(currentUserId, user.getId());

        dto.setFollowing(following);

        return dto;
        }).toList();
    }
}
