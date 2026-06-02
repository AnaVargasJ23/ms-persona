package com.onclass.persona.domain.spi;

import com.onclass.persona.domain.model.Bootcamp;
import com.onclass.persona.domain.model.Persona;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPersonaPersistencePort {
    Mono<Persona> guardar(Persona persona);
    Mono<Persona> buscarPorId(Long id);
    Mono<Boolean> existePorCorreo(String correo);
    Flux<Bootcamp> obtenerBootcampsDePersona(Long personaId);
    Mono<Void> inscribirEnBootcamp(Long personaId, Long bootcampId);
    Mono<Long> contarBootcampsDePersona(Long personaId);
}
