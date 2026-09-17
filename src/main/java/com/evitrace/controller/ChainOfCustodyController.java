package com.evitrace.controller;

import com.evitrace.dto.CustodyRequest;
import com.evitrace.dto.CustodyResponse;
import com.evitrace.service.ChainOfCustodyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/custody")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ChainOfCustodyController {

    private final ChainOfCustodyService custodyService;

    // Record a new custody action (collected, transferred, examined, etc.)
    @PostMapping
    public ResponseEntity<CustodyResponse> recordAction(@Valid @RequestBody CustodyRequest request) {
        return ResponseEntity.ok(custodyService.recordAction(request));
    }

    // Get the full chain for one evidence item (ordered oldest → newest)
    @GetMapping("/evidence/{evidenceId}")
    public ResponseEntity<List<CustodyResponse>> getChain(@PathVariable Long evidenceId) {
        return ResponseEntity.ok(custodyService.getCustodyChain(evidenceId));
    }

    // Verify the integrity of an evidence item's chain
    @GetMapping("/evidence/{evidenceId}/verify")
    public ResponseEntity<Map<String, Object>> verifyIntegrity(@PathVariable Long evidenceId) {
        boolean intact = custodyService.verifyChainIntegrity(evidenceId);
        return ResponseEntity.ok(Map.of(
                "evidenceId", evidenceId,
                "integrityVerified", intact,
                "message", intact
                        ? "All custody records have consistent hashes — evidence is unaltered."
                        : "HASH MISMATCH DETECTED — evidence may have been tampered with."
        ));
    }
}