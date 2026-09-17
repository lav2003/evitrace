package com.evitrace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data                  // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor      // Lombok: generates a no-args constructor (JPA requires this)
@AllArgsConstructor     // Lombok: generates a constructor with all fields
@Builder                // Lombok: lets you do User.builder().name("x").build()
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;  // stored as a BCrypt hash, never plain text

    @Column(name = "badge_number", unique = true)
    private String badgeNumber;

    @Enumerated(EnumType.STRING)   // stores the enum as "ADMIN" text, not 0/1/2/3 numbers
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist   // JPA lifecycle hook: runs automatically right before this row is first saved
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
