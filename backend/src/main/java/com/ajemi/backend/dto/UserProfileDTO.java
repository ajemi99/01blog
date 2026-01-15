package com.ajemi.backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserProfileDTO {
    private String username;
    private Long id;
    // private String bio;
    private long postsCount;
    private long followersCount;
    private long followingCount;
    
    // Had l-joj homa l-Logic kamel:
    private boolean isOwner;      // True ila knti nta moula l-profile
    private boolean isFollowing;  // True ila knti m-followi had s-siyyd
    
    private List<PostResponseDTO> posts;
}
