package com.sentinelrisk.sentinelrisk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Este método salta AUTOMÁTICAMENTE cuando @Valid falla
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        System.out.println("⚠️ ALERTA DE SEGURIDAD: Se intentó enviar datos inválidos.");
        System.out.println("❌ Errores detectados: " + errors);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}