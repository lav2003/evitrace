package com.evitrace.service;

import com.evitrace.dto.EvidenceResponse;
import com.evitrace.model.Case;
import com.evitrace.model.Evidence;
import com.evitrace.model.EvidenceStatus;
import com.evitrace.model.EvidenceType;
import com.evitrace.model.User;
import com.evitrace.repository.CaseRepository;
import com.evitrace.repository.EvidenceRepository;
import com.evitrace.repository.UserRepository;
import com.evitrace.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EvidenceService {

    private final EvidenceRepository evidenceRepository;
    private final CaseRepository caseRepository;
    private final UserRepository userRepository;
    private final HashUtil hashUtil;
    private final AuditLogService auditLogService; // ADD THIS

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    public EvidenceResponse uploadEvidence(
            Long caseId,
            String title,
            EvidenceType type,
            String description,
            MultipartFile file
    ) {
        Case evidenceCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Case not found with id: " + caseId));

        User currentUser = getCurrentUser();

        try {
            byte[] fileBytes = file.getBytes();

            String hash = hashUtil.computeSha256(new ByteArrayInputStream(fileBytes));

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String storedFilename = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path destination = uploadPath.resolve(storedFilename);
            Files.copy(new ByteArrayInputStream(fileBytes), destination, StandardCopyOption.REPLACE_EXISTING);

            Evidence evidence = Evidence.builder()
                    .evidenceCase(evidenceCase)
                    .title(title)
                    .type(type)
                    .description(description)
                    .collectedBy(currentUser)
                    .filePath(destination.toString())
                    .fileHash(hash)
                    .build();

            Evidence saved = evidenceRepository.save(evidence);

            // Log the upload
            auditLogService.log(
                currentUser.getEmail(),
                "EVIDENCE_UPLOADED",
                "Evidence", saved.getId(),
                "Title: " + title + ", Type: " + type + ", Case: " + evidenceCase.getCaseNumber(),
                "N/A"
            );

            return mapToResponse(saved);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store evidence file", e);
        }
    }

    public List<EvidenceResponse> getEvidenceForCase(Long caseId) {
        Case evidenceCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new IllegalArgumentException("Case not found with id: " + caseId));

        return evidenceRepository.findByEvidenceCase(evidenceCase)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public EvidenceResponse updateStatus(Long id, EvidenceStatus newStatus) {
        Evidence evidence = evidenceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evidence not found with id: " + id));

        User currentUser = getCurrentUser();

        evidence.setStatus(newStatus);
        Evidence updated = evidenceRepository.save(evidence);

        // Log the status change
        auditLogService.log(
            currentUser.getEmail(),
            "EVIDENCE_STATUS_CHANGED",
            "Evidence", updated.getId(),
            "New status: " + newStatus,
            "N/A"
        );

        return mapToResponse(updated);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));
    }

    private EvidenceResponse mapToResponse(Evidence e) {
        return EvidenceResponse.builder()
                .id(e.getId())
                .caseId(e.getEvidenceCase().getId())
                .caseNumber(e.getEvidenceCase().getCaseNumber())
                .title(e.getTitle())
                .type(e.getType())
                .description(e.getDescription())
                .collectedByName(e.getCollectedBy().getName())
                .collectedAt(e.getCollectedAt())
                .fileHash(e.getFileHash())
                .status(e.getStatus())
                .build();
    }
}