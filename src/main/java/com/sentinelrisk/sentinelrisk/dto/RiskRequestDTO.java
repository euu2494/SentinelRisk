package com.sentinelrisk.sentinelrisk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RiskRequestDTO (
        @NotBlank(message = "El título no puede estar vacío")
        @Size(min = 5, max = 100, message = "El título debe tener entre 5 y 100 carácteres")
        String title,

        @NotBlank(message = "La descripción no puede estar vacía")
        String description,

        @NotBlank(message = "El responsable (owner) es obligatorio")
                String owner,

                String observation
){}


