package com.ajemi.backend.service;

import com.ajemi.backend.entity.Post;
import com.ajemi.backend.entity.User;
import com.ajemi.backend.entity.Role.RoleName;
import com.ajemi.backend.repository.PostRepository;
import com.ajemi.backend.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

import com.ajemi.backend.entity.Role;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 🟢 Ajouter un post
    public Post createPost(String username, String content, String mediaUrl) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setMediaUrl(mediaUrl);

        return postRepository.save(post);
    }

    // 🟡 Récupérer tous les posts
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 🔴 Supprimer un post par ID
    public void deletePost(Long id, String username) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post introuvable"));

    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

    // ✅ Vérifier si le user est le créateur OU admin
    boolean isOwner = post.getUser().getUsername().equals(username);
    boolean isAdmin = user.getRole().getName() == Role.RoleName.ADMIN;

    if (!isOwner && !isAdmin) {
        throw new RuntimeException("❌ Vous n'avez pas la permission de supprimer ce post !");
    }

    postRepository.delete(post);
    }
}
