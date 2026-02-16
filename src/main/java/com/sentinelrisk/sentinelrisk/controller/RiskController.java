package com.sentinelrisk.sentinelrisk.controller;

import com.sentinelrisk.sentinelrisk.entity.Risk;
import com.sentinelrisk.sentinelrisk.entity.Status;
import com.sentinelrisk.sentinelrisk.entity.Priority;
import com.sentinelrisk.sentinelrisk.service.RiskService;
import org.springframework.beans.factory.annotation.Autowired;
import com.sentinelrisk.sentinelrisk.dto.AnalysisRequestDTO;
import org.springframework.web.bind.annotation.*;
import com.sentinelrisk.sentinelrisk.dto.RiskRequestDTO;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/risks")
public class RiskController {

    @Autowired
    private RiskService riskService;

    //Lectura y filtros de búsqueda
    @GetMapping
    public List<Risk> getRisks(
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Status status) {

        if (priority != null && status != null) {
            return riskService.getRisksByPriorityAndStatus(priority, status);
        } else if (priority != null) {
            return riskService.getRisksByPriority(priority);
        } else if (status != null) {
            return riskService.getRisksByStatus(status);
        } else {
            return riskService.getAllRisks();
        }
    }

    // Escritura
    @PostMapping
    public ResponseEntity<Risk> createRisk(@Valid @RequestBody RiskRequestDTO riskDTO) {
        Risk risk = new Risk();
        risk.setTitle(riskDTO.title());
        risk.setDescription(riskDTO.description());
        risk.setOwner(riskDTO.owner());
        risk.setObservation(riskDTO.observation());

        // Lo ponemos explícito para evitar fallos de validación
        risk.setStatus(Status.OPEN);

        Risk savedRisk = riskService.processAndSave(risk);
        return ResponseEntity.ok(savedRisk);
    }
    // Para eliminar
    @DeleteMapping("/{id}")
    public void deleteRisk(@PathVariable Long id) {
        riskService.deleteRisk(id);
    }

    // Para filtrar por ID
    @GetMapping("/{id}")
    public Risk getRiskById(@PathVariable Long id) {
        return riskService.getRiskById(id);
    }

    // Para updatear
    @PutMapping("/{id}")
    public Risk updatedRisk(@PathVariable Long id, @RequestBody Risk risk) {
        return riskService.updateRisk(id, risk);
    }

    // Este endpoint recibe el DTO blindado, valida los datos y llama a la IA
    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeRiskWithAI(@Valid @RequestBody AnalysisRequestDTO analysisDTO) {

        System.out.println(" IA: Recibida solicitud de análisis de riesgo.");
        System.out.println(" Validación correcta. Categoría: " + analysisDTO.getCategory());

        return ResponseEntity.ok(riskService.analyzeRisk(analysisDTO));
    }
}