package com.evitrace.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who did it (email from JWT)
    private String actorEmail;

    // What action: "EVIDENCE_UPLOADED", "CASE_CREATED", "LOGIN", etc.
    private String action;

    // Which entity was affected: "Evidence", "Case", "User"
    private String entityType;

    // The ID of that entity (e.g. evidence ID 5)
    private Long entityId;

    // Extra detail — what changed, what was submitted
    @Column(length = 1000)
    private String details;

    // IP address of the request
    private String ipAddress;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }
}