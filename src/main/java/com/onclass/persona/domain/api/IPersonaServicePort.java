package com.onclass.persona.domain.api;

import com.onclass.persona.domain.model.Persona;
import reactor.core.publisher.Mono;

public interface IPersonaServicePort {
    Mono<Persona> registrar(Persona persona);
    Mono<Void> inscribirEnBootcamp(Long personaId, Long bootcampId);
}
