package com.evitrace.controller;

import com.evitrace.dto.CaseResponse;
import com.evitrace.dto.CreateCaseRequest;
import com.evitrace.model.CaseStatus;
import com.evitrace.service.CaseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CaseController {

    private final CaseService caseService;

    @PreAuthorize("hasAnyRole('INVESTIGATOR', 'ADMIN')")
    @PostMapping
    public ResponseEntity<CaseResponse> createCase(@Valid @RequestBody CreateCaseRequest request) {
        CaseResponse response = caseService.createCase(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CaseResponse>> getAllCases() {
        return ResponseEntity.ok(caseService.getAllCases());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaseResponse> getCaseById(@PathVariable Long id) {
        return ResponseEntity.ok(caseService.getCaseById(id));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<CaseResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam CaseStatus status
    ) {
        return ResponseEntity.ok(caseService.updateStatus(id, status));
    }
}