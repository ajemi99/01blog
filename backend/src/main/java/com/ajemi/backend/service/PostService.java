package com.ajemi.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ajemi.backend.dto.PostResponseDTO;
import com.ajemi.backend.entity.Notification.NotificationType;
import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.exception.ApiException;
import com.ajemi.backend.repository.FollowRepository;
import com.ajemi.backend.repository.LikeRepository;
import com.ajemi.backend.repository.PostRepository;
import com.ajemi.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final FollowRepository followRepository;
    private final NotificationService notificationService;
    private final LikeRepository likeRepository;
    // ===============================
    // Create a new post
    // ===============================
    @Transactional
    public PostResponseDTO createPost(String username, String description, MultipartFile file) {
        // 1️⃣ Get user from DB
        User actor = userRepository.findByUsername(username)
               .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
         if (actor.isBanned()) {
            throw new ApiException("Your account is suspended. You cannot post.", HttpStatus.FORBIDDEN);
        }

        // 2️⃣ Store media file (image/video)
        String mediaUrl = fileStorageService.saveFile(file);

        // 3️⃣ Create Post object
        Post post = new Post();
        post.setAuthor(actor);
        post.setDescription(description);
        post.setMediaUrl(mediaUrl);

        // 4️⃣ Save Post in DB
        Post saved = postRepository.save(post);
        List<User> followers = followRepository.findFollowersByUser(actor);

        notificationService.sendNotificationsToFollowers(followers, actor, NotificationType.POST);

        // 5️⃣ Map Entity → DTO
        return mapToDTO(saved,username);
    }

    // ===============================
    // Convert Post entity to DTO
    // ===============================
public PostResponseDTO mapToDTO(Post post, String currentUsername) {
    PostResponseDTO dto = new PostResponseDTO();
    dto.setId(post.getId());
    dto.setDescription(post.getDescription());
    dto.setMediaUrl(post.getMediaUrl());
    dto.setCreatedAt(post.getCreatedAt());
    dto.setAuthorUsername(post.getAuthor().getUsername());
    dto.setAuthorId(post.getAuthor().getId());
    dto.setUpdatedAt(post.getUpdatedAt());

    // 🚀 counts ghadi i-welliw khfif b-sabab @BatchSize li derna f l-Entity
    dto.setLikesCount(post.getLikes().size());
    dto.setCommentCount(post.getComments().size());

    // 🚩 Check nichan mn d-Database bla loop dyal stream().anyMatch()
    // Had l-methode a7san mn streams hit kadd-dir "EXISTS" f SQL (khfifa)
    if (currentUsername != null) {
        dto.setLiked(likeRepository.existsByPostIdAndUserUsername(post.getId(), currentUsername));
    } else {
        dto.setLiked(false);
    }

    return dto;
}
    
@Transactional
    public void deletePost(@NonNull Long id, String username) {

    Post post = postRepository.findById( id)
            .orElseThrow(() -> new ApiException("Post not found", HttpStatus.NOT_FOUND));
        if (!post.getAuthor().getUsername().equals(username)) {
           throw new ApiException("You don't have permission to modify this post", HttpStatus.FORBIDDEN);
        }
    // 🚩 1. Khbi smiya dial l-file qbel ma t-msa7 l-post
        String fileToDelete = post.getMediaUrl();

        // 🚩 2. Msa7 mn d-Database hya l-lowla
        postRepository.delete(post);

        // 🚩 3. Ila dāzt DB bla machakil, 3ad msa7 l-file mn l-disk
        if (fileToDelete != null) {
            fileStorageService.deleteFile(fileToDelete);
        }
}
// ===============================
// Update Post (description + optional file)
// ===============================
@Transactional
public PostResponseDTO updatePost(@NonNull Long id, String username,  String newDescription, MultipartFile newFile) {

    // 1️⃣ جيب البوست من DB
    Post post = postRepository.findById( id)
            .orElseThrow(() ->  new ApiException("Post not found", HttpStatus.NOT_FOUND));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new ApiException("You don't have permission to modify this post", HttpStatus.FORBIDDEN);
        }
    // 2️⃣ Update description فقط إلا كانت ماشي null
    if (newDescription != null  && !newDescription.isBlank()) {
        post.setDescription(newDescription);
    }

    // 3️⃣ إلا كان user بغا يبدل الصورة
    if (newFile != null && !newFile.isEmpty()) {

        // 🗑️ مسح الصورة القديمة إلا كانت موجودة
        fileStorageService.deleteFile(post.getMediaUrl());

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

@Transactional(readOnly = true)
public Page<PostResponseDTO> getFeed(String username,int page, int size) {
    // 1. Jbed smiya dial l-user li m-login
   

    // 2. Jbed l-user mn DB (bach n-t-akkdo rah mzyan)
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));

    // 3. Jbed l-IDs dial nass li m-followihom
    List<Long> followingIds = followRepository.findFollowingIds(user.getId());
    
    // 4. Zid l-ID dyalk bach tchouf 7ta posts dyalk f l-Feed
    followingIds.add(user.getId());

    // 5. Jbed l-posts kāmline dial l-feed m-rattbin mn l-jdid l-qdim
   // 3. Cree l-objet Pageable (ordering kiy-tra dakhil l-query a7san)
   int safeSize = (size > 50) ? 50 : size;
    
    // Check raqm l-page bach may-sift-ch raqm naqes (-1)
    int safePage = (page < 0) ? 0 : page;
    Pageable pageable = PageRequest.of(safePage, safeSize);

    // 4. Jbed l-page dial l-posts
    Page<Post> postsPage = postRepository.findAllByAuthor_IdInOrderByCreatedAtDesc(followingIds, pageable);

    // 5. Converti l-Page dyal Entities l Page dyal DTOs (mapToDTO)
    return postsPage.map(post -> mapToDTO(post, username));
}

}

