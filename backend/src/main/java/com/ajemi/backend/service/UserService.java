package com.ajemi.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ajemi.backend.dto.InfoDto;
import com.ajemi.backend.dto.PostResponseDTO;
import com.ajemi.backend.dto.UserProfileDTO;
import com.ajemi.backend.dto.UserSearchDTO;
import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.exception.ApiException;
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

    public User findUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
    }

    public List<UserSearchDTO> searchUsers(String username, Long currentUserId) {
        // 1. Jbed ga3 l-users li kiy-matchiw s-smiya
        List<User> users = userRepository.findByUsernameContainingIgnoreCaseAndIdNot(username, currentUserId);
        
        // 2. Jbed ga3 l-IDs li m-followihom l-user mra wa7da (1 Query)
        // Ila kān currentUserId null (machi m-login), rjje3 list khawya
        List<Long> followingIds = (currentUserId != null) 
                ? followRepository.findFollowingIdsByFollowerId(currentUserId) 
                : List.of();

        // 3. Map to DTO
        return users.stream().map(user -> {
            UserSearchDTO dto = new UserSearchDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            
            // Check ghir f l-memory (List.contains kadd-khdem f O(n))
            dto.setFollowing(followingIds.contains(user.getId()));
            
            return dto;
        }).collect(Collectors.toList());
    }
        public UserProfileDTO getUserProfile(String username, Long currentUserId, int page, int size) {
        // 1. Jbed l-user men l-DB b s-smiya
        User targetUser = userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND)); // 👈 Khdem b had l-class
                User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        if (targetUser.isBanned()) {
             throw new ApiException("User not found", HttpStatus.NOT_FOUND);
        }


        UserProfileDTO dto = new UserProfileDTO();
        dto.setUsername(targetUser.getUsername());
            dto.setId(targetUser.getId());
        // 2. Check: Wach hada houwa ana?
        // Ila kante l-ID dyali kat-tswa l-ID dial s-siyyd li f l-URL
        dto.setOwner(currentUserId != null && targetUser.getId().equals(currentUserId));

        // 3. Check: Wach m-followih? (Ghir ila kante machi owner)
        if (!dto.isOwner()) {
            boolean following = followRepository.existsByFollowerIdAndFollowingId(currentUserId, targetUser.getId());
            dto.setFollowing(following);
        }

        // 4. Jbed counts (N-t-khaylou 3ndek had l-methat f repositories)
        dto.setPostsCount(postRepository.countByAuthor_Id(targetUser.getId()));
        dto.setFollowersCount(followRepository.countFollowers(targetUser.getId()));
        dto.setFollowingCount(followRepository.countFollowing(targetUser.getId()));
           int safeSize = (size > 50) ? 50 : size;
            int safePage = (page < 0) ? 0 : page;
         Pageable pageable = PageRequest.of(safePage, safeSize);
        // 5. Jbed l-posts dialu
         Page<Post> postsEntityPage =
            postRepository.findByAuthor_IdOrderByCreatedAtDesc(targetUser.getId(),pageable);

        // 6. Converti l-Page dyal Entities l Page dyal DTOs
        Page<PostResponseDTO> postsDtoPage = postsEntityPage.map(post -> 
            postService.mapToDTO(post, currentUser.getUsername())
        );
        dto.setPosts(postsDtoPage);

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
