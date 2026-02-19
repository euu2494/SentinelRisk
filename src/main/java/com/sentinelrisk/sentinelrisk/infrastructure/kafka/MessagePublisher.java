package com.sentinelrisk.sentinelrisk.infrastructure.kafka;

public interface MessagePublisher {
    void sendMessage(String message);
}