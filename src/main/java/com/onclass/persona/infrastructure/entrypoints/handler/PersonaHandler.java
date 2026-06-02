package com.onclass.persona.infrastructure.entrypoints.handler;

import com.onclass.persona.domain.api.IPersonaServicePort;
import com.onclass.persona.domain.excepcion.PersonaException;
import com.onclass.persona.infrastructure.entrypoints.dto.InscripcionResponse;
import com.onclass.persona.infrastructure.entrypoints.dto.PersonaRegistradaResponse;
import com.onclass.persona.infrastructure.entrypoints.dto.PersonaRequest;
import com.onclass.persona.infrastructure.entrypoints.mapper.PersonaMapper;
import com.onclass.persona.infrastructure.entrypoints.util.ErrorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersonaHandler {

    private final IPersonaServicePort personaServicePort;
    private final PersonaMapper personaMapper;

    public Mono<ServerResponse> registrar(ServerRequest request) {
        return request.bodyToMono(PersonaRequest.class)
                .map(personaMapper::toDomain)
                .flatMap(personaServicePort::registrar)
                .flatMap(saved -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(PersonaRegistradaResponse.builder()
                                .id(saved.getId())
                                .nombre(saved.getNombre())
                                .correo(saved.getCorreo())
                                .mensaje("Persona registrada exitosamente")
                                .build()))
                .onErrorResume(PersonaException.class, e -> {
                    log.error("Error de negocio: {}", e.getMessage());
                    return ServerResponse
                            .status(HttpStatus.BAD_REQUEST)
                            .bodyValue(ErrorDTO.builder()
                                    .code(e.getCode())
                                    .message(e.getMessage())
                                    .build());
                });
    }

    public Mono<ServerResponse> inscribir(ServerRequest request) {
        Long personaId = Long.valueOf(request.pathVariable("personaId"));
        Long bootcampId = Long.valueOf(request.pathVariable("bootcampId"));
        return personaServicePort.inscribirEnBootcamp(personaId, bootcampId)
                .then(ServerResponse.ok()
                        .bodyValue(InscripcionResponse.builder()
                                .personaId(personaId)
                                .bootcampId(bootcampId)
                                .mensaje("Inscripción exitosa")
                                .build()))
                .onErrorResume(PersonaException.class, e -> {
                    log.error("Error de negocio: {}", e.getMessage());
                    return ServerResponse
                            .status(HttpStatus.BAD_REQUEST)
                            .bodyValue(ErrorDTO.builder()
                                    .code(e.getCode())
                                    .message(e.getMessage())
                                    .build());
                });
    }
}
