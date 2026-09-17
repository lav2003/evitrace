package com.evitrace.service;

import com.evitrace.model.AuditLog;
import com.evitrace.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    // Called by other services to write a log entry
    public void log(String actorEmail, String action,
                    String entityType, Long entityId,
                    String details, String ipAddress) {
        AuditLog entry = new AuditLog();
        entry.setActorEmail(actorEmail);
        entry.setAction(action);
        entry.setEntityType(entityType);
        entry.setEntityId(entityId);
        entry.setDetails(details);
        entry.setIpAddress(ipAddress);
        auditLogRepository.save(entry);
    }

    public List<AuditLog> getLogsByActor(String email) {
        return auditLogRepository.findByActorEmailOrderByTimestampDesc(email);
    }

    public List<AuditLog> getLogsForEntity(String entityType, Long entityId) {
        return auditLogRepository
            .findByEntityTypeAndEntityIdOrderByTimestampDesc(entityType, entityId);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}

