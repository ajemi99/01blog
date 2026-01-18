package com.ajemi.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ajemi.backend.dto.InfoDto;
import com.ajemi.backend.dto.UserProfileDTO;
import com.ajemi.backend.dto.UserSearchDTO;
import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.FollowRepository;
import com.ajemi.backend.repository.PostRepository;
import com.ajemi.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final PostService postService;

     public User findUser(long  userId) {
        return userRepository.findById(userId)
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
        public UserProfileDTO getUserProfile(String username, Long currentUserId) {
        // 1. Jbed l-user men l-DB b s-smiya
        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileDTO dto = new UserProfileDTO();
        dto.setUsername(targetUser.getUsername());
            dto.setId(targetUser.getId());
        // 2. Check: Wach hada houwa ana?
        // Ila kante l-ID dyali kat-tswa l-ID dial s-siyyd li f l-URL
        dto.setOwner(targetUser.getId().equals(currentUserId));

        // 3. Check: Wach m-followih? (Ghir ila kante machi owner)
        if (!dto.isOwner()) {
            boolean following = followRepository.existsByFollowerIdAndFollowingId(currentUserId, targetUser.getId());
            dto.setFollowing(following);
        }

        // 4. Jbed counts (N-t-khaylou 3ndek had l-methat f repositories)
        dto.setPostsCount(postRepository.countByAuthor_Id(targetUser.getId()));
        dto.setFollowersCount(followRepository.countFollowers(targetUser.getId()));
        dto.setFollowingCount(followRepository.countFollowing(targetUser.getId()));

        // 5. Jbed l-posts dialu
    List<Post> posts =
            postRepository.findByAuthor_IdOrderByCreatedAtDesc(targetUser.getId());

        dto.setPosts(
            posts.stream()
                .map((Post post) -> postService.mapToDTO(post, targetUser.getUsername()))
                .collect(Collectors.toList())
        );


        return dto;
    }
    public InfoDto getCurrentUser(Long userId){
        User user = this.findUser(userId);
        Long followersCount = followRepository.countFollowers(userId);
        Long followingCount = followRepository.countFollowing(userId);
        return new InfoDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                followingCount,
                followersCount,
                user.getRole().getName()
        );
    }
}
