package com.ajemi.backend.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.ajemi.backend.entity.Role;
import com.ajemi.backend.dto.PostResponseDTO;
import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.repository.PostRepository;
import com.ajemi.backend.repository.UserRepository;
import com.ajemi.backend.repository.FollowRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final FollowRepository followRepository;

    // ===============================
    // Create a new post
    // ===============================
    @Transactional
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
        return mapToDTO(saved,username);
    }

    // ===============================
    // Get all posts (feed)
    // ===============================
@Transactional(readOnly = true)
public List<PostResponseDTO> getAllPostsForAdmin(String adminUsername) {
    try {
       User admin = userRepository.findByUsername(adminUsername)
        .orElseThrow(() -> new RuntimeException("User not found"));

        if (admin.getRole() == null || admin.getRole().getName() != Role.RoleName.ADMIN) {
            throw new RuntimeException("Forbidden");
        }

        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(post -> mapToDTO(post, adminUsername))
                .toList();
    } catch (Exception e) {
        e.printStackTrace(); // هنا غادي تشوف الخطأ فـ console ديال Spring
        throw e;
    }
}


    // ===============================
    // Convert Post entity to DTO
    // ===============================
   private PostResponseDTO mapToDTO(Post post, String currentUsername) {
    PostResponseDTO dto = new PostResponseDTO();
    dto.setId(post.getId());
    dto.setDescription(post.getDescription());
    dto.setMediaUrl(post.getMediaUrl());
    dto.setCreatedAt(post.getCreatedAt());
    dto.setAuthorUsername(post.getAuthor().getUsername());
    dto.setAuthorId(post.getAuthor().getId());
    dto.setUpdatedAt(post.getUpdatedAt());

    // عدد likes
    dto.setLikesCount(post.getLikes().size());

    // واش user دار like
    boolean liked = post.getLikes().stream()
            .anyMatch(like -> like.getUser().getUsername().equals(currentUsername));
    dto.setLiked(liked);

    return dto;
}
    
@Transactional
    public void deletePost(Long id, String username) {

    Post post = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));
         if (!post.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }
    // احذف الملف إذا كان موجود
        deleteFile(post.getMediaUrl());

    // حذف البوست من قاعدة البيانات
    postRepository.delete(post);
}
// ===============================
// Update Post (description + optional file)
// ===============================
@Transactional
public PostResponseDTO updatePost(Long id, String username,  String newDescription, MultipartFile newFile) {

    // 1️⃣ جيب البوست من DB
    Post post = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }
    // 2️⃣ Update description فقط إلا كانت ماشي null
    if (newDescription != null  && !newDescription.isBlank()) {
        post.setDescription(newDescription);
    }

    // 3️⃣ إلا كان user بغا يبدل الصورة
    if (newFile != null && !newFile.isEmpty()) {

        // 🗑️ مسح الصورة القديمة إلا كانت موجودة
        deleteFile(post.getMediaUrl());

        // 💾 حفظ الصورة الجديدة
        String newMediaUrl = fileStorageService.saveFile(newFile);
        post.setMediaUrl(newMediaUrl);
    }

    // 4️⃣ update timestamp
    post.setUpdatedAt(java.time.LocalDateTime.now());

    // 5️⃣ Save changes
    Post updated = postRepository.save(post);

    return mapToDTO(updated,username);
}
private void deleteFile(String mediaUrl) {
    if (mediaUrl != null) {
        File file = new File("." + mediaUrl);
        if (file.exists()) file.delete();
    }
}
@Transactional(readOnly = true)
public List<PostResponseDTO> getMyPosts(Long authorId, String username) {
    List<Post> posts = postRepository.findByAuthor_IdOrderByCreatedAtDesc(authorId);
    return posts.stream()
                .map(post -> mapToDTO(post, username))
                .collect(Collectors.toList());
}
@Transactional(readOnly = true)
public List<PostResponseDTO> getFeed(Authentication authentication) {

    String username = authentication.getName();

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    List<Long> followingIds = new ArrayList<>(
            followRepository.findFollowingIds(user.getId())
    );
    followingIds.add(user.getId());

    return postRepository
            .findAllByAuthor_IdInOrderByCreatedAtDesc(followingIds)
            .stream()
            .map(post -> mapToDTO(post, username))
            .toList();
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
