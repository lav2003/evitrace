package com.evitrace.controller;

import com.evitrace.model.AuditLog;
import com.evitrace.service.AuditLogService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @GetMapping("/actor/{email}")
    public ResponseEntity<List<AuditLog>> getByActor(@PathVariable String email) {
        return ResponseEntity.ok(auditLogService.getLogsByActor(email));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @GetMapping("/entity/{type}/{id}")
    public ResponseEntity<List<AuditLog>> getByEntity(
            @PathVariable String type,
            @PathVariable Long id) {
        return ResponseEntity.ok(auditLogService.getLogsForEntity(type, id));
    }
}