package com.sentinelrisk.sentinelrisk.domain.risk;

import com.sentinelrisk.sentinelrisk.common.dto.MessageResponseDTO;
import com.sentinelrisk.sentinelrisk.domain.risk.dto.AnalysisRequestDTO;
import com.sentinelrisk.sentinelrisk.domain.risk.dto.AnalysisResponseDTO;
import com.sentinelrisk.sentinelrisk.domain.risk.dto.RiskRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risks")
public class RiskController {

    private final RiskService riskService;
    private final RisksAnalysisService risksAnalysisService;

    public RiskController(RiskService riskService, RisksAnalysisService risksAnalysisService) {
        this.riskService = riskService;
        this.risksAnalysisService = risksAnalysisService;
    }

    @GetMapping
    public ResponseEntity<List<Risk>> getRisks(
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Status status) {

        return ResponseEntity.ok(riskService.getFilteredRisks(priority, status));
    }

    @PostMapping
    public ResponseEntity<Risk> createRisk(@Valid @RequestBody RiskRequestDTO riskDTO) {
        return ResponseEntity.ok(riskService.createRisk(riskDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Risk> getRiskById(@PathVariable Long id) {
        return ResponseEntity.ok(riskService.getRiskById(id));
    }

    @PatchMapping("/{id}/status-priority")
    public ResponseEntity<Risk> updateStatusAndPriority(
            @PathVariable Long id,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Status status) {
        return ResponseEntity.ok(riskService.quickUpdate(id, priority, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteRisk(@PathVariable Long id) {
        riskService.deleteRisk(id);
        return ResponseEntity.ok(new MessageResponseDTO("Riesgo eliminado con éxito: " + id));
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponseDTO> analyzeRisk(@Valid @RequestBody AnalysisRequestDTO request) {
        Priority calculatedPriority = risksAnalysisService.analyzePriority(
                request.title(),
                request.description()
        );

        return ResponseEntity.ok(new AnalysisResponseDTO(
                calculatedPriority,
                "Sentinel AI Hybrid Engine",
                "Análisis de prioridad completado"
        ));
    }
}