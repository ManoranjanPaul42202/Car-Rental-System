package com.rental.car.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId; // Auto-incremented primary key

    @Column(name = "username", unique = true, nullable = false)
    private String username; // Unique username

    @Column(name = "password", nullable = false)
    private String password; // Hashed password

    @Column(name = "enabled", nullable = false)
    private int enabled;

    @Column(name = "email", nullable = false)
    private String email;
}
