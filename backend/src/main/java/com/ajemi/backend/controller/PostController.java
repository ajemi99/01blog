    package com.ajemi.backend.controller;
    
    import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
    import org.springframework.lang.NonNull;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.web.bind.annotation.DeleteMapping;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.PutMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
    import org.springframework.web.multipart.MultipartFile;

    import  com.ajemi.backend.dto.PostResponseDTO;
    import com.ajemi.backend.security.UserDetailsImpl;
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
        public ResponseEntity<PostResponseDTO> createPost(
                @AuthenticationPrincipal UserDetailsImpl userDetails,
                @RequestPart(required = false) String description,
                @RequestPart(required = false) MultipartFile file)
        {
                // Validation sghira f l-bedya
                if ((description == null || description.trim().isEmpty()) && (file == null || file.isEmpty())) {
                    return ResponseEntity.badRequest().build();
                }
                    PostResponseDTO dto = postService.createPost(userDetails.getUsername(), description, file);
                    return ResponseEntity.ok(dto);
        }


            @DeleteMapping("/{id}")
            public ResponseEntity<Void> deletePost(@PathVariable @NonNull Long id,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
                postService.deletePost(id, userDetails.getUsername());
                        return ResponseEntity.ok().build();
            }
        @PutMapping(value = "/{id}", consumes = "multipart/form-data")
        public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable @NonNull Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart(required = false) String description,
            @RequestPart(required = false) MultipartFile file

        ) {
            PostResponseDTO updatedPost = postService.updatePost(id, userDetails.getUsername(), description, file);
             return ResponseEntity.ok(updatedPost);
           }

        @GetMapping("/feed")
        public ResponseEntity<Page<PostResponseDTO>> getFeed(
                @AuthenticationPrincipal UserDetailsImpl userDetails,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {
            // Passi l-page w l-size l l-service
            return ResponseEntity.ok(postService.getFeed(userDetails.getUsername(), page, size));
        }
    }


