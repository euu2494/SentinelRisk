package com.sentinelrisk.sentinelrisk.domain.risk;

import com.sentinelrisk.sentinelrisk.domain.risk.dto.RiskRequestDTO;
import com.sentinelrisk.sentinelrisk.infrastructure.kafka.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RiskService {

    private static final Logger log = LoggerFactory.getLogger(RiskService.class);

    private final RiskRepository riskRepository;
    private final MessagePublisher messagePublisher;
    private final RisksAnalysisService risksAnalysisService;
    private final RiskMapper riskMapper;

    public RiskService(RiskRepository riskRepository,
                       MessagePublisher messagePublisher,
                       RisksAnalysisService risksAnalysisService,
                       RiskMapper riskMapper) {
        this.riskRepository = riskRepository;
        this.messagePublisher = messagePublisher;
        this.risksAnalysisService = risksAnalysisService;
        this.riskMapper = riskMapper;
    }

    public Risk createRisk(RiskRequestDTO dto) {
        Risk risk = riskMapper.toEntity(dto);
        risk.setStatus(Status.OPEN);
        return processAndSave(risk);
    }

    public Risk processAndSave(Risk risk) {
        Priority decision = risksAnalysisService.analyzePriority(risk.getTitle(), risk.getDescription());
        risk.setPriority(decision);

        Risk savedRisk = riskRepository.save(risk);
        checkAndSendAlert(savedRisk, "RIESGO CRÍTICO DETECTADO: ");

        return savedRisk;
    }

    public List<Risk> getFilteredRisks(Priority priority, Status status) {
        if (priority != null && status != null) return riskRepository.findByPriorityAndStatus(priority, status);
        if (priority != null) return riskRepository.findByPriority(priority);
        if (status != null) return riskRepository.findByStatus(status);

        return riskRepository.findAll();
    }

    public Risk getRiskById(Long id) {
        return riskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Riesgo no encontrado con id: " + id));
    }

    public Risk updateRisk(Long id, Risk updatedRisk) {
        return riskRepository.findById(id).map(existingRisk -> {
            existingRisk.setTitle(updatedRisk.getTitle());
            existingRisk.setDescription(updatedRisk.getDescription());
            existingRisk.setOwner(updatedRisk.getOwner());
            existingRisk.setStatus(updatedRisk.getStatus());

            Priority newPriority = risksAnalysisService.analyzePriority(existingRisk.getTitle(), existingRisk.getDescription());
            existingRisk.setPriority(newPriority);

            Risk saved = riskRepository.save(existingRisk);
            checkAndSendAlert(saved, "RIESGO ACTUALIZADO A CRÍTICO: ");

            return saved;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID no encontrado: " + id));
    }

    public Risk quickUpdate(Long id, Priority priority, Status status) {
        return riskRepository.findById(id).map(risk -> {
            if (priority != null) risk.setPriority(priority);
            if (status != null) risk.setStatus(status);

            Risk saved = riskRepository.save(risk);
            log.info("Actualización manual de estado/prioridad para el riesgo ID: {}", id);

            checkAndSendAlert(saved, "RIESGO MARCADO MANUALMENTE COMO CRÍTICO: ");

            return saved;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ID no encontrado: " + id));
    }

    public void deleteRisk(Long id) {
        if (!riskRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID no encontrado: " + id);
        }
        riskRepository.deleteById(id);
    }

    private void checkAndSendAlert(Risk risk, String prefix) {
        if (risk != null && risk.getPriority() == Priority.CRITICAL) {
            String message = prefix + risk.getTitle() + " [Responsable: " + risk.getOwner() + "]";
            log.info("Publicando alerta crítica en Kafka para riesgo: {}", risk.getId());
            messagePublisher.sendMessage(message);
        }
    }
}