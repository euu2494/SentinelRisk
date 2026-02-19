package com.sentinelrisk.sentinelrisk.domain.risk;

import com.sentinelrisk.sentinelrisk.domain.risk.strategy.AiRiskAnalysisStrategy;
import com.sentinelrisk.sentinelrisk.domain.risk.strategy.LocalRiskAnalysisStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RisksAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(RisksAnalysisService.class);

    private final LocalRiskAnalysisStrategy localStrategy;
    private final AiRiskAnalysisStrategy aiStrategy;

    public RisksAnalysisService(LocalRiskAnalysisStrategy localStrategy, AiRiskAnalysisStrategy aiStrategy) {
        this.localStrategy = localStrategy;
        this.aiStrategy = aiStrategy;
    }

    public Priority analyzePriority(String title, String description) {
        Priority localDecision = localStrategy.analyze(title, description);

        if (localDecision == Priority.CRITICAL) {
            log.info("Prioridad CRITICAL detectada por motor local para: {}", title);
            return Priority.CRITICAL;
        }

        try {
            log.info("Consultando motor de IA para análisis profundo: {}", title);
            return aiStrategy.analyze(title, description);
        } catch (Exception e) {
            log.error("Fallo en el motor de IA. Activando modo Fallback con resultado local para: {}", title);
            return localDecision;
        }
    }
}