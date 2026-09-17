package com.evitrace.dto;

import com.evitrace.model.EvidenceStatus;
import com.evitrace.model.EvidenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class EvidenceResponse {

    private Long id;
    private Long caseId;
    private String caseNumber;
    private String title;
    private EvidenceType type;
    private String description;
    private String collectedByName;
    private LocalDateTime collectedAt;
    private String fileHash;
    private EvidenceStatus status;
}