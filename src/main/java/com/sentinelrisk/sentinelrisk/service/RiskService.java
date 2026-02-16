package com.sentinelrisk.sentinelrisk.service;

import com.sentinelrisk.sentinelrisk.entity.Priority;
import com.sentinelrisk.sentinelrisk.entity.Risk;
import com.sentinelrisk.sentinelrisk.entity.Status;
import com.sentinelrisk.sentinelrisk.repository.RiskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.sentinelrisk.sentinelrisk.dto.AnalysisRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskService {

    @Autowired
    private RiskRepository riskRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private RisksAnalysisService risksAnalysisService;

   public Risk processAndSave(Risk risk) {

       Priority decision = risksAnalysisService.analyzePriority(risk.getTitle(), risk.getDescription());
       risk.setPriority(decision);

       Risk savedRisk = riskRepository.save(risk);

       checkAndSendAlert(savedRisk, "🚨 RIESGO CRÍTICO DETECTADO: ");

       return savedRisk;
   }

    public List<Risk> getAllRisks() {
        return riskRepository.findAll();
    }

    public Risk getRiskById(Long id) {
        return riskRepository.findById(id).orElse(null);
    }

    //Donde se actualiza
    public Risk updateRisk(Long id, Risk updatedRisk) {
        return riskRepository.findById(id).map(existingRisk -> {
            existingRisk.setTitle(updatedRisk.getTitle());
            existingRisk.setDescription(updatedRisk.getDescription());
            existingRisk.setPriority(updatedRisk.getPriority());
            existingRisk.setStatus(updatedRisk.getStatus());
            existingRisk.setOwner(updatedRisk.getOwner());

            Risk saved = riskRepository.save(existingRisk);

            checkAndSendAlert(saved, " RIESGO ACTUALIZADO A CRÍTICO: ");

            return saved;
        }).orElse(null);
    }

    public void deleteRisk(Long id) {
        riskRepository.deleteById(id);
    }

    public List<Risk> getRisksByPriority(Priority priority) {
        return riskRepository.findByPriority(priority);
    }

    public List<Risk> getRisksByStatus(Status status) {
        return riskRepository.findByStatus(status);
    }

    public List<Risk> getRisksByPriorityAndStatus(Priority priority, Status status) {
        return riskRepository.findByPriorityAndStatus(priority, status);
    }

    private void checkAndSendAlert(Risk risk, String prefix) {
        if (risk != null && risk.getPriority() == Priority.CRITICAL) {
            String message = prefix + risk.getTitle() + " [Responsable: " + risk.getOwner() + "]";
            kafkaProducerService.sendMessage(message);
        }
    }


    public String analyzeRisk(AnalysisRequestDTO analysisDTO) {
        System.out.println("Service: Solicitud de análisis directo recibida.");

        Priority decision = risksAnalysisService.analyzePriority("Análisis Directo", analysisDTO.getRiskDescription());

        System.out.println("Categoría: " + analysisDTO.getCategory() + " | Prioridad DETERMINADA: " + decision);

        return "Análisis IA SentinelRisk: El riesgo descrito '" + analysisDTO.getRiskDescription() +
                "' ha sido evaluado bajo la categoría " + analysisDTO.getCategory() +
                ". La prioridad determinada es: " + decision + ".";
    }
}