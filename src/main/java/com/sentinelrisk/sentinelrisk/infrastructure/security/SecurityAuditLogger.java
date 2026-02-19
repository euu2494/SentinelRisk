package com.sentinelrisk.sentinelrisk.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class SecurityAuditLogger {

    private static final Logger logger = LoggerFactory.getLogger(SecurityAuditLogger.class);

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        logger.info("[AUDIT - LOGIN ÉXITO] Usuario autenticado: {}", username);
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        String username = event.getAuthentication().getName();
        String error = event.getException().getMessage();

        logger.warn("[AUDIT - LOGIN FALLIDO] Intento de acceso sospechoso. Usuario: {} | Motivo: {}",
                username, error);
    }
}