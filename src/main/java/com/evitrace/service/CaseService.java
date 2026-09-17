package com.evitrace.service;

import com.evitrace.dto.CaseResponse;
import com.evitrace.dto.CreateCaseRequest;
import com.evitrace.model.Case;
import com.evitrace.model.CaseStatus;
import com.evitrace.model.User;
import com.evitrace.repository.CaseRepository;
import com.evitrace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CaseService {

    private final CaseRepository caseRepository;
    private final UserRepository userRepository;

    public CaseResponse createCase(CreateCaseRequest request) {
        if (caseRepository.existsByCaseNumber(request.getCaseNumber())) {
            throw new IllegalArgumentException("Case number already exists");
        }

        User currentUser = getCurrentUser();

        Case newCase = Case.builder()
                .caseNumber(request.getCaseNumber())
                .title(request.getTitle())
                .description(request.getDescription())
                .createdBy(currentUser)
                .build();

        Case saved = caseRepository.save(newCase);
        return mapToResponse(saved);
    }

    public List<CaseResponse> getAllCases() {
        return caseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public CaseResponse getCaseById(Long id) {
        Case foundCase = caseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Case not found with id: " + id));
        return mapToResponse(foundCase);
    }

    public CaseResponse updateStatus(Long id, CaseStatus newStatus) {
        Case existingCase = caseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Case not found with id: " + id));

        existingCase.setStatus(newStatus);
        Case updated = caseRepository.save(existingCase);
        return mapToResponse(updated);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));
    }

    private CaseResponse mapToResponse(Case c) {
        return CaseResponse.builder()
                .id(c.getId())
                .caseNumber(c.getCaseNumber())
                .title(c.getTitle())
                .description(c.getDescription())
                .status(c.getStatus())
                .createdByName(c.getCreatedBy().getName())
                .createdAt(c.getCreatedAt())
                .build();
    }
}