package com.sentinelrisk.sentinelrisk.service;

import com.sentinelrisk.sentinelrisk.entity.Priority;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RisksAnalysisService {

    private final ChatLanguageModel chatModel;
    
    private final List<String> CRITICAL_KEYWORDS = List.of(
            "hack", "ataque", "sql", "inyect", "malware", "virus", "ransomware",
            "comprometido", "brecha", "denegación", "ddos", "rootkit", "exploit",

            "base de datos", "database", "servidor caído", "caída total",
            "pérdida de datos", "borrado", "corrupción", "explotó", "offline",

            "fuego", "incendio", "bomba", "inundación", "explosión", "terremoto",
            "catastrófico", "humo", "cortocircuito",

            "urgente", "crítico", "emergencia", "inmediato", "detener", "parada"
    );

    private final List<String> HIGH_KEYWORDS = List.of(
            "fallo", "error", "bug", "lento", "vulnerabilidad", "advertencia",
            "riesgo", "sospechoso", "anomalía", "incidencia", "no funciona",
            "bloqueo", "acceso denegado", "password", "seguridad"
    );

    public RisksAnalysisService(@Value("${gemini.api.key}") String apiKey) {
        this.chatModel = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .temperature(0.0)
                .build();
    }

    public Priority analyzePriority(String title, String description) {
        String content = (title + " " + description).toLowerCase();

        for (String keyword : CRITICAL_KEYWORDS) {
            if (content.contains(keyword)) {
                System.out.println("[GUARDÍAN]  Prioridad CRITICAL detectada por palabra: " + keyword);
                return Priority.CRITICAL;
            }
        }

        try {
            System.out.println("[IA] Consultando a Gemini para: " + title);

            String prompt = String.format(
                    "Eres un experto en ciberseguridad y gestión de riesgos. " +
                            "Evalúa la urgencia de este incidente: '%s'. " +
                            "Descripción: '%s'. " +
                            "Responde SOLO con una de estas palabras: LOW, MEDIUM, HIGH, CRITICAL.",
                    title, description
            );

            String response = chatModel.generate(prompt).trim().toUpperCase();

            try {
                return Priority.valueOf(response);
            } catch (IllegalArgumentException e) {
                if (response.contains("CRITICAL")) return Priority.CRITICAL;
                if (response.contains("HIGH")) return Priority.HIGH;
                if (response.contains("MEDIUM")) return Priority.MEDIUM;
                return Priority.LOW;
            }

        } catch (Exception e) {
            System.err.println("[FALLO IA] Entrando en modo de emergencia (Fallback)");

            for (String keyword : HIGH_KEYWORDS) {
                if (content.contains(keyword)) {
                    return Priority.HIGH;
                }
            }
            return Priority.LOW;
        }
    }
}