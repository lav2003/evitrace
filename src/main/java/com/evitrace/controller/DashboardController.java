package com.evitrace.controller;

import com.evitrace.repository.AuditLogRepository;
import com.evitrace.repository.CaseRepository;
import com.evitrace.repository.ChainOfCustodyRepository;
import com.evitrace.repository.EvidenceRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final CaseRepository caseRepository;
    private final EvidenceRepository evidenceRepository;
    private final ChainOfCustodyRepository chainOfCustodyRepository;
    private final AuditLogRepository auditLogRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(Map.of(
            "totalCases",     caseRepository.count(),
            "totalEvidence",  evidenceRepository.count(),
            "totalTransfers", chainOfCustodyRepository.count(),
            "totalAuditLogs", auditLogRepository.count()
        ));
    }
}