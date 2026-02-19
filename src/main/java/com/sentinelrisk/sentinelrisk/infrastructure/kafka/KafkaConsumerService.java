package com.sentinelrisk.sentinelrisk.infrastructure.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService implements RiskMessageProcessor {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Override
    @KafkaListener(topics = "risk-alerts", groupId = "sentinel-group")
    public void process(String message) {
        log.info("[KAFKA] Mensaje recibido desde el tópico: {}", message);
    }
}