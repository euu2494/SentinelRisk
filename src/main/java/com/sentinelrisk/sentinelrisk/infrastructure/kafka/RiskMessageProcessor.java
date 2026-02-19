package com.sentinelrisk.sentinelrisk.infrastructure.kafka;

public interface RiskMessageProcessor {
    void process(String message);
}