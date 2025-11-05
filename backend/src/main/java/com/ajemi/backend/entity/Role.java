package com.ajemi.backend.entity;

import jakarta.persistence.*;
// import java.util.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // ex: "ROLE_USER" ou "ROLE_ADMIN"

    public Role() {}
    public Role(String name) { this.name = name; }

    // getters et setters
}