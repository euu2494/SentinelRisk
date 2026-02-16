package com.sentinelrisk.sentinelrisk.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "risks-topic", groupId = "sentinel-group")
    public void consume(String message) {
        System.out.println("\n--- [NUEVO MENSAJE RECIBIDO DESDE KAFKA] ---");
        System.out.println("Procesando alerta" + message);
        System.out.println("----------------------------------------------\n");
    }
}
