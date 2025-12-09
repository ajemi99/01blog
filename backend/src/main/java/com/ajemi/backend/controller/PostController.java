package com.ajemi.backend.controller;

import com.ajemi.backend.dto.PostResponseDTO;
import com.ajemi.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // ===============================
    // Create a new post
    // ===============================
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createPost(
            Authentication auth,
            @RequestPart(required = false) String description,
            @RequestPart(required = false) MultipartFile file
    ) {
         if ((description == null || description.trim().isEmpty()) && 
        (file == null || file.isEmpty())) {

        return ResponseEntity.badRequest().body("Post cannot be empty!");
    }
        // 1️⃣ Get username from JWT token
        String username = auth.getName();

        // 2️⃣ Call service to create post
        PostResponseDTO dto = postService.createPost(username, description, file);

        // 3️⃣ Return the DTO as response
        return ResponseEntity.ok(dto);
    }

    // ===============================
    // Get all posts (feed)
    // ===============================
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting post: " + e.getMessage());
        }
    }
}












// package com.ajemi.backend.controller;

// import com.ajemi.backend.dto.PostDTO;
// import com.ajemi.backend.entity.Post;
// import com.ajemi.backend.service.PostService;

// import org.springframework.web.bind.annotation.*;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.security.core.userdetails.UserDetails;

// import java.util.List;

// @RestController
// @RequestMapping("/api/posts")

// public class PostController {

//     private final PostService postService;

//     public PostController(PostService postService) {
//         this.postService = postService;
//     }

//     @PostMapping
// public ResponseEntity<?> createPost(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Post post) {
//     try {
//         Post savedPost = postService.createPost(userDetails.getUsername(), post.getContent(), post.getMediaUrl());

//         // Create DTO
//         PostDTO dto = new PostDTO();
//         dto.setId(savedPost.getId());
//         dto.setContent(savedPost.getContent());
//         dto.setMediaUrl(savedPost.getMediaUrl());
//         dto.setUsername(userDetails.getUsername());

//         return ResponseEntity.ok(dto);
//     } catch (Exception e) {
//         e.printStackTrace();
//         return ResponseEntity.status(500).body("❌ Erreur lors de la création du post: " + e.getMessage());
//     }
// }
//     @GetMapping
//     public List<Post> getAllPosts() {
//         return postService.getAllPosts();
//     }

//     @DeleteMapping("/{id}")
//     public void deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
//         postService.deletePost(id, userDetails.getUsername());
//     }
// }
