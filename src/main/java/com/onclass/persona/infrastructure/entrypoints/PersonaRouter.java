package com.onclass.persona.infrastructure.entrypoints;

import com.onclass.persona.infrastructure.entrypoints.dto.PersonaRequest;
import com.onclass.persona.infrastructure.entrypoints.handler.PersonaHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Tag(name = "Persona", description = "Gestión de personas e inscripciones On-Class")
public class PersonaRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/personas",
                    method = RequestMethod.POST,
                    beanClass = PersonaHandler.class,
                    beanMethod = "registrar",
                    operation = @Operation(
                            operationId = "registrarPersona",
                            summary = "Registrar una nueva persona",
                            tags = {"Persona"},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = PersonaRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Persona registrada exitosamente"),
                                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/personas/{personaId}/bootcamps/{bootcampId}",
                    method = RequestMethod.POST,
                    beanClass = PersonaHandler.class,
                    beanMethod = "inscribir",
                    operation = @Operation(
                            operationId = "inscribirEnBootcamp",
                            summary = "Inscribir persona en un bootcamp",
                            tags = {"Persona"},
                            parameters = {
                                    @io.swagger.v3.oas.annotations.Parameter(
                                            name = "personaId",
                                            in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
                                            required = true,
                                            description = "ID de la persona"
                                    ),
                                    @io.swagger.v3.oas.annotations.Parameter(
                                            name = "bootcampId",
                                            in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
                                            required = true,
                                            description = "ID del bootcamp"
                                    )
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Inscripción exitosa"),
                                    @ApiResponse(responseCode = "400", description = "Error de negocio")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> personaRoutes(PersonaHandler handler) {
        return RouterFunctions.route()
                .POST("/api/v1/personas", handler::registrar)
                .POST("/api/v1/personas/{personaId}/bootcamps/{bootcampId}", handler::inscribir)
                .build();
    }
}
