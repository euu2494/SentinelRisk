package com.sentinelrisk.sentinelrisk.domain.risk.dto;

import com.sentinelrisk.sentinelrisk.domain.risk.Priority;

public record AnalysisResponseDTO(
        Priority priority,
        String engine,
        String message
) {
}