package com.sentinelrisk.sentinelrisk.domain.risk.strategy;

import com.sentinelrisk.sentinelrisk.domain.risk.Priority;

public interface RiskAnalysisStrategy {
    Priority analyze(String title, String description);
}