package com.saurabh.crm.lead;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LeadRequest(
    @NotBlank @Size(max = 120) String contactName,
    @NotBlank @Size(max = 160) String companyName,
    @Email @Size(max = 160) String email,
    @Size(max = 30) String phone,
    @Size(max = 80) String industry,
    @Size(max = 80) String leadSource,
    LeadStatus status,
    @PositiveOrZero BigDecimal estimatedValue,
    @Size(max = 120) String assignedTo,
    LocalDate nextFollowUpDate,
    @Size(max = 1000) String notes
) {}
