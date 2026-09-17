package com.evitrace.controller;

import com.evitrace.dto.EvidenceResponse;
import com.evitrace.model.EvidenceStatus;
import com.evitrace.model.EvidenceType;
import com.evitrace.service.EvidenceService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/evidence")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class EvidenceController {

    private final EvidenceService evidenceService;

    @PreAuthorize("hasAnyRole('INVESTIGATOR', 'ADMIN')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<EvidenceResponse> uploadEvidence(
            @RequestParam Long caseId,
            @RequestParam String title,
            @RequestParam EvidenceType type,
            @RequestParam(required = false) String description,
            @RequestParam("file") MultipartFile file
    ) {
        EvidenceResponse response = evidenceService.uploadEvidence(caseId, title, type, description, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<EvidenceResponse>> getEvidenceForCase(@PathVariable Long caseId) {
        return ResponseEntity.ok(evidenceService.getEvidenceForCase(caseId));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMIN', 'FORENSIC_ANALYST')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<EvidenceResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam EvidenceStatus status
    ) {
        return ResponseEntity.ok(evidenceService.updateStatus(id, status));
        
    }
}