package com.ajemi.backend.entity;

import java.time.Instant;

import jakarta.persistence.*;
// import lombok.Data;

// import java.time.LocalDateTime;
// import java.util.*;

// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "posts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 3000)
    private String description;

    private String mediaUrl;

    private Instant createdAt = Instant.now();

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
}


// @Entity
// @Table(name = "posts")
// @Data
// @JsonIgnoreProperties({"comments", "likes"})
// public class Post {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne
 
//     @JoinColumn(name = "user_id")
//     private User user;

//     @Column(columnDefinition = "TEXT")
//     private String content;

//     private String mediaUrl;

//     private LocalDateTime createdAt = LocalDateTime.now();

//     // 🔗 Relations
//     @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
//     private List<Comment> comments = new ArrayList<>();

//     @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
//     private List<Like> likes = new ArrayList<>();

//     public Post() {}

//     // getters et setters
// }