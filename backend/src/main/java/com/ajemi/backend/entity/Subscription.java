package com.ajemi.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
// import java.time.LocalDateTime;
@Getter @Setter
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id")
    private User following;

    // private LocalDateTime createdAt = LocalDateTime.now();

    public Subscription() {}

    // getters et setters
}