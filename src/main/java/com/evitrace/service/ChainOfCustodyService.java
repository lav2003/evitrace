package com.evitrace.service;

import com.evitrace.dto.CustodyRequest;
import com.evitrace.dto.CustodyResponse;
import com.evitrace.model.*;
import com.evitrace.repository.ChainOfCustodyRepository;
import com.evitrace.repository.EvidenceRepository;
import com.evitrace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChainOfCustodyService {

    private final ChainOfCustodyRepository custodyRepository;
    private final EvidenceRepository evidenceRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService; // ADD THIS

    public CustodyResponse recordAction(CustodyRequest request) {
        // 1. Find the evidence item
        Evidence evidence = evidenceRepository.findById(request.getEvidenceId())
                .orElseThrow(() -> new RuntimeException("Evidence not found"));

        // 2. Find the currently logged-in user (the "actor")
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User actor = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Build the custody record
        ChainOfCustody record = new ChainOfCustody();
        record.setEvidence(evidence);
        record.setActor(actor);
        record.setAction(request.getAction());
        record.setNotes(request.getNotes());
        record.setIntegrityHash(evidence.getFileHash());

        // 4. Save it
        ChainOfCustody saved = custodyRepository.save(record);

        // 5. Log the custody action
        auditLogService.log(
            actor.getEmail(),
            "CUSTODY_ACTION",
            "Evidence", evidence.getId(),
            "Action: " + request.getAction() + ", Notes: " + request.getNotes(),
            "N/A"
        );

        return toResponse(saved);
    }

    public List<CustodyResponse> getCustodyChain(Long evidenceId) {
        return custodyRepository.findByEvidenceIdOrderByTimestampAsc(evidenceId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public boolean verifyChainIntegrity(Long evidenceId) {
        List<ChainOfCustody> chain = custodyRepository
                .findByEvidenceIdOrderByTimestampAsc(evidenceId);

        if (chain.isEmpty()) return false;

        String expectedHash = chain.get(0).getIntegrityHash();
        return chain.stream()
                .allMatch(c -> c.getIntegrityHash().equals(expectedHash));
    }

    private CustodyResponse toResponse(ChainOfCustody c) {
        CustodyResponse r = new CustodyResponse();
        r.setId(c.getId());
        r.setEvidenceId(c.getEvidence().getId());
        r.setEvidenceTitle(c.getEvidence().getTitle());
        r.setActorName(c.getActor().getName());
        r.setAction(c.getAction());
        r.setNotes(c.getNotes());
        r.setIntegrityHash(c.getIntegrityHash());
        r.setTimestamp(c.getTimestamp());
        return r;
    }
}