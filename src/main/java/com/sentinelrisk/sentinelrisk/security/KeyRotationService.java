package com.sentinelrisk.sentinelrisk.security;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KeyRotationService {

    private final Map<String, SecretKey> keyStore = new ConcurrentHashMap<>();

    private String currentKeyId;

    @PostConstruct
    public void init() {
        rotateKey();
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void rotateKey() {
        String newKeyId = UUID.randomUUID().toString();

        SecretKey newKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

        keyStore.put(newKeyId, newKey);

        this.currentKeyId = newKeyId;

        if (keyStore.size() > 3) {
            keyStore.keySet().stream()
                    .filter(id -> !id.equals(currentKeyId))
                    .findFirst()
                    .ifPresent(keyStore::remove);
        }

        System.out.println("[SEGURIDAD - KEY ROTATION] Claves rotadas. Nueva Master Key ID: " + currentKeyId);
    }

    public SecretKey getCurrentKey() {
        return keyStore.get(currentKeyId);
    }

    public String getCurrentKeyId() {
        return currentKeyId;
    }

    public SecretKey getKey(String keyId) {
        return keyStore.get(keyId);
    }
}