package com.saurabh.crm.activity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record ActivityRequest(
    @NotNull ActivityType type,
    @NotBlank @Size(max = 1000) String summary,
    @Size(max = 120) String createdBy,
    LocalDateTime happenedAt,
    LocalDateTime nextActionAt
) {}
