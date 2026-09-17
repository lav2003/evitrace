package com.evitrace.dto;

import com.evitrace.model.CustodyAction;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustodyRequest {

    @NotNull
    private Long evidenceId;

    @NotNull
    private CustodyAction action;

    private String notes;
}