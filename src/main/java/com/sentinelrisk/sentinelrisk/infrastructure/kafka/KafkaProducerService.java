package com.sentinelrisk.sentinelrisk.infrastructure.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService implements MessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String TOPIC_NAME = "risk-alerts";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(String message) {
        log.info("Intentando enviar mensaje al tópico [{}]: {}", TOPIC_NAME, message);

        this.kafkaTemplate.send(TOPIC_NAME, message).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Mensaje enviado con éxito al offset: {}", result.getRecordMetadata().offset());
            } else {
                log.error("Error al enviar mensaje a Kafka. Motivo: {}", ex.getMessage(), ex);
            }
        });
    }
}