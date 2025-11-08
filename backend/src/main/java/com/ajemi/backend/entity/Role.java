package com.ajemi.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // باش نخزن الاسم كـ "ADMIN" أو "USER"
    @Column(nullable = false, unique = true)
    private RoleName name;

    public enum RoleName {
        USER,
        ADMIN
    }
}
