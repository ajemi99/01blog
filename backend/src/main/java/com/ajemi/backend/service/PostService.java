package com.ajemi.backend.service;

import com.ajemi.backend.dto.PostResponseDTO;
import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.PostRepository;
import com.ajemi.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    // ===============================
    // Create a new post
    // ===============================
    public PostResponseDTO createPost(String username, String description, MultipartFile file) {

        // 1️⃣ Get user from DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Store media file (image/video)
        String mediaUrl = fileStorageService.saveFile(file);

        // 3️⃣ Create Post object
        Post post = new Post();
        post.setAuthor(user);
        post.setDescription(description);
        post.setMediaUrl(mediaUrl);

        // 4️⃣ Save Post in DB
        Post saved = postRepository.save(post);

        // 5️⃣ Map Entity → DTO
        return mapToDTO(saved);
    }

    // ===============================
    // Get all posts (feed)
    // ===============================
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ===============================
    // Convert Post entity to DTO
    // ===============================
    private PostResponseDTO mapToDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setDescription(post.getDescription());
        dto.setMediaUrl(post.getMediaUrl());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setAuthorUsername(post.getAuthor().getUsername());
        // dto.setLikes(post.getLikes());
        return dto;
    }
}





// package com.ajemi.backend.service;

// import com.ajemi.backend.entity.Post;
// import com.ajemi.backend.entity.User;
// // import com.ajemi.backend.entity.Role.RoleName;
// import com.ajemi.backend.repository.PostRepository;
// import com.ajemi.backend.repository.UserRepository;

// import org.springframework.stereotype.Service;

// import java.util.List;

// import com.ajemi.backend.entity.Role;

// @Service
// public class PostService {
//     private final PostRepository postRepository;
//     private final UserRepository userRepository;

//     public PostService(PostRepository postRepository, UserRepository userRepository) {
//         this.postRepository = postRepository;
//         this.userRepository = userRepository;
//     }

//     // 🟢 Ajouter un post
//     public Post createPost(String username, String content, String mediaUrl) {
//         User user = userRepository.findByUsername(username)
//                 .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

//         Post post = new Post();
//         post.setUser(user);
//         post.setContent(content);
//         post.setMediaUrl(mediaUrl);

//         return postRepository.save(post);
//     }

//     // 🟡 Récupérer tous les posts
//     public List<Post> getAllPosts() {
//         return postRepository.findAll();
//     }

//     // 🔴 Supprimer un post par ID
//     public void deletePost(Long id, String username) {
//         Post post = postRepository.findById(id)
//             .orElseThrow(() -> new RuntimeException("Post introuvable"));

//     User user = userRepository.findByUsername(username)
//             .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

//     // ✅ Vérifier si le user est le créateur OU admin
//     boolean isOwner = post.getUser().getUsername().equals(username);
//     boolean isAdmin = user.getRole().getName() == Role.RoleName.ADMIN;

//     if (!isOwner && !isAdmin) {
//         throw new RuntimeException("❌ Vous n'avez pas la permission de supprimer ce post !");
//     }

//     postRepository.delete(post);
//     }
// }
