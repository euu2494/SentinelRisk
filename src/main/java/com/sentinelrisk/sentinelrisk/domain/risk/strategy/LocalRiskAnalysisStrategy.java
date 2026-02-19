package com.sentinelrisk.sentinelrisk.domain.risk.strategy;

import com.sentinelrisk.sentinelrisk.domain.risk.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocalRiskAnalysisStrategy implements RiskAnalysisStrategy {

    private static final Logger log = LoggerFactory.getLogger(LocalRiskAnalysisStrategy.class);

    private static final List<String> CRITICAL_KEYWORDS = List.of(
            "hack", "ataque", "sql", "inyect", "malware", "virus", "ransomware",
            "comprometido", "brecha", "denegación", "ddos", "rootkit", "exploit",
            "base de datos", "database", "servidor caído", "caída total",
            "pérdida de datos", "borrado", "corrupción", "explotó", "offline",
            "fuego", "incendio", "bomba", "inundación", "explosión", "terremoto",
            "catastrófico", "humo", "cortocircuito",
            "urgente", "crítico", "emergencia", "inmediato", "detener", "parada"
    );

    private static final List<String> HIGH_KEYWORDS = List.of(
            "fallo", "error", "bug", "lento", "vulnerabilidad", "advertencia",
            "riesgo", "sospechoso", "anomalía", "incidencia", "no funciona",
            "bloqueo", "acceso denegado", "password", "seguridad"
    );

    @Override
    public Priority analyze(String title, String description) {
        String content = (title + " " + description).toLowerCase();

        for (String keyword : CRITICAL_KEYWORDS) {
            if (content.contains(keyword)) {
                log.info("[DETECCIÓN LOCAL] Riesgo CRÍTICO identificado por palabra clave: '{}'", keyword);
                return Priority.CRITICAL;
            }
        }

        for (String keyword : HIGH_KEYWORDS) {
            if (content.contains(keyword)) {
                log.info("[DETECCIÓN LOCAL] Riesgo ALTO identificado por palabra clave: '{}'", keyword);
                return Priority.HIGH;
            }
        }

        return Priority.LOW;
    }
}