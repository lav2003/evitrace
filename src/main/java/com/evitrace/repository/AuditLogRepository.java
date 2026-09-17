package com.evitrace.repository;

import com.evitrace.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // All logs for one actor (e.g. show what ravi@evitrace.com did)
    List<AuditLog> findByActorEmailOrderByTimestampDesc(String actorEmail);

    // All logs for one entity type + ID (e.g. all actions on Evidence #3)
    List<AuditLog> findByEntityTypeAndEntityIdOrderByTimestampDesc(
        String entityType, Long entityId
    );
}