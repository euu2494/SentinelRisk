package com.sentinelrisk.sentinelrisk.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SentinelRisk API",
                version = "1.0",
                description = "Documentación oficial con Login Automático."
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.OAUTH2,
        description = "Autenticación automática: Introduce tus credenciales para obtener el token.",
        flows = @OAuthFlows(
                password = @OAuthFlow(
                        tokenUrl = "/api/auth/login"
                )
        )
)
public class OpenApiConfig {
}