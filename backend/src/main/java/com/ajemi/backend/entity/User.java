package com.ajemi.backend.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
@JsonIgnoreProperties({"posts", "comments", "likes", "following", "followers", "notifications", "reportsSent", "reportsReceived"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String bio;
    private String profilePicture;
    private LocalDateTime createdAt = LocalDateTime.now();

    // 🧩 علاقة ManyToOne: كل User عندو Role واحد
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id") // هاد العمود غادي يتزاد فـ جدول users
    private Role role ;

    // 📝 Relation avec les posts
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
   
    private List<Post> posts = new ArrayList<>();

    // 💬 Relation avec les commentaires
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    // ❤️ Relation avec les likes
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    // 👥 Relation avec les abonnements
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<Subscription> following = new ArrayList<>();

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL)
    private List<Subscription> followers = new ArrayList<>();

    // 🔔 Notifications
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    // 🚨 Signalements
    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL)
    private List<Report> reportsSent = new ArrayList<>();

    @OneToMany(mappedBy = "reportedUser", cascade = CascadeType.ALL)
    private List<Report> reportsReceived = new ArrayList<>();
    

    // 🧱 Constructeurs, getters, setters
    public User() {}

    // getters et setters ici
}
