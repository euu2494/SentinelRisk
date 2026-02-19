package com.sentinelrisk.sentinelrisk.domain.risk.dto;

import jakarta.validation.constraints.NotBlank;

public record AnalysisRequestDTO(
        @NotBlank(message = "El título es obligatorio") String title,
        @NotBlank(message = "La descripción es obligatoria") String description
) {
}