package com.evitrace.dto;

import com.evitrace.model.CaseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class CaseResponse {

    private Long id;
    private String caseNumber;
    private String title;
    private String description;
    private CaseStatus status;
    private String createdByName;
    private LocalDateTime createdAt;
}