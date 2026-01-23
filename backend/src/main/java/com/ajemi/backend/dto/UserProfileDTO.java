package com.ajemi.backend.dto;

import org.springframework.data.domain.Page;

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
    private boolean owner;      // True ila knti nta moula l-profile
    private boolean following;  // True ila knti m-followi had s-siyyd
    
    private Page<PostResponseDTO> posts;
}
