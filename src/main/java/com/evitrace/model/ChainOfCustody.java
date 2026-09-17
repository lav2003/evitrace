package com.evitrace.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "chain_of_custody")
@Data
public class ChainOfCustody {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which evidence item this record belongs to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evidence_id", nullable = false)
    private Evidence evidence;

    // The person who performed this action
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private User actor;

    // What action happened
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustodyAction action;

    // Free-text reason or note
    @Column(length = 500)
    private String notes;

    // SHA-256 hash of the evidence file AT THIS MOMENT — proves file wasn't changed
    @Column(nullable = false)
    private String integrityHash;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }
}