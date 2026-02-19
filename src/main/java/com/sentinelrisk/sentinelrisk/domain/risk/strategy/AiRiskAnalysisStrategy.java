package com.sentinelrisk.sentinelrisk.domain.risk.strategy;

import com.sentinelrisk.sentinelrisk.domain.risk.Priority;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AiRiskAnalysisStrategy implements RiskAnalysisStrategy {

    private static final Logger log = LoggerFactory.getLogger(AiRiskAnalysisStrategy.class);
    private final ChatLanguageModel chatModel;

    public AiRiskAnalysisStrategy(@Value("${gemini.api.key}") String apiKey) {
        this.chatModel = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .temperature(0.0)
                .build();
    }

    @Override
    public Priority analyze(String title, String description) {
        log.info("Iniciando análisis profundo con IA para: {}", title);

        String prompt = String.format(
                "Eres un experto en ciberseguridad y gestión de riesgos. " +
                        "Evalúa la urgencia de este incidente: '%s'. " +
                        "Descripción: '%s'. " +
                        "Responde ÚNICAMENTE con una de estas palabras: LOW, MEDIUM, HIGH, CRITICAL.",
                title, description
        );

        String response = chatModel.generate(prompt).trim().toUpperCase();
        log.info("Respuesta recibida de la IA: {}", response);

        try {
            return Priority.valueOf(response);
        } catch (IllegalArgumentException e) {
            log.warn("La IA devolvió un formato no estándar. Intentando mapeo manual de: {}", response);
            if (response.contains("CRITICAL")) return Priority.CRITICAL;
            if (response.contains("HIGH")) return Priority.HIGH;
            if (response.contains("MEDIUM")) return Priority.MEDIUM;
            return Priority.LOW;
        }
    }
}