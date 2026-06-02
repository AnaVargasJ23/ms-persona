package com.onclass.persona.infrastructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "MS Persona - On Class",
        version = "1.0",
        description = "Microservicio para gestión de personas e inscripciones"
    )
)
public class OpenApiConfig {
}
