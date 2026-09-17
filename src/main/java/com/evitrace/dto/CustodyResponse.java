package com.evitrace.dto;

import com.evitrace.model.CustodyAction;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustodyResponse {
    private Long id;
    private Long evidenceId;
    private String evidenceTitle;
    private String actorName;      // who performed the action
    private CustodyAction action;
    private String notes;
    private String integrityHash;
    private LocalDateTime timestamp;
}