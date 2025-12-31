    package com.ajemi.backend.controller;
    
    import java.util.List;

    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.web.bind.annotation.DeleteMapping;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.ajemi.backend.security.UserDetailsImpl;
import com.ajemi.backend.dto.PostResponseDTO;

import com.ajemi.backend.service.PostService;

import lombok.RequiredArgsConstructor;


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
                @AuthenticationPrincipal UserDetailsImpl userDetails,
                @RequestPart(required = false) String description,
                @RequestPart(required = false) MultipartFile file
        ) {
            if ((description == null || description.trim().isEmpty()) && 
            (file == null || file.isEmpty())) {

            return ResponseEntity.badRequest().body("Post cannot be empty!");
        }
            String username = userDetails.getUsername(); // أو userDetails.getId() إذا بغيت
            PostResponseDTO dto = postService.createPost(username, description, file);

            // 3️⃣ Return the DTO as response
            return ResponseEntity.ok(dto);
        }


@DeleteMapping("/{id}")
public ResponseEntity<?> deletePost(@PathVariable Long id,
                                    Authentication authentication) {
    try {
        String username = authentication.getName(); // من JWT
        postService.deletePost(id, username);
        return ResponseEntity.ok().build();
    } catch (RuntimeException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error deleting post");
    }
}
        @PutMapping(value = "/{id}", consumes = "multipart/form-data")
        public ResponseEntity<?> updatePost(
            Authentication auth,
            @PathVariable Long id,
             @RequestPart(required = false) String description,
            @RequestPart(required = false) MultipartFile file,
            Authentication authentication
        ) {
    try {
        String username = authentication.getName(); // من JWT

        return ResponseEntity.ok(
                postService.updatePost(id, username, description, file)
        );

    } catch (RuntimeException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error updating post");
    }
        }


        @GetMapping("/my-posts/{userId}")
    public List<PostResponseDTO> getMyPosts(@PathVariable Long userId,Authentication auth) {
         String username = auth.getName();
        return postService.getMyPosts(userId,username);
    }

    @GetMapping("/feed")
    public List<PostResponseDTO> getFeed(Authentication authentication) {
        return postService.getFeed(authentication);
    }
    }


