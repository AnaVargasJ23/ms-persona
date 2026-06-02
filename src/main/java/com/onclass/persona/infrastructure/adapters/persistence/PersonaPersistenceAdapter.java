package com.onclass.persona.infrastructure.adapters.persistence;

import com.onclass.persona.domain.model.Bootcamp;
import com.onclass.persona.domain.model.Persona;
import com.onclass.persona.domain.spi.IPersonaPersistencePort;
import com.onclass.persona.infrastructure.adapters.persistence.entity.PersonaBootcampEntity;
import com.onclass.persona.infrastructure.adapters.persistence.mapper.PersonaEntityMapper;
import com.onclass.persona.infrastructure.adapters.persistence.repository.PersonaBootcampR2dbcRepository;
import com.onclass.persona.infrastructure.adapters.persistence.repository.PersonaR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PersonaPersistenceAdapter implements IPersonaPersistencePort {

    private final PersonaR2dbcRepository personaRepository;
    private final PersonaBootcampR2dbcRepository personaBootcampRepository;
    private final PersonaEntityMapper personaEntityMapper;

    @Override
    @Transactional
    public Mono<Persona> guardar(Persona persona) {
        return personaRepository.save(personaEntityMapper.toEntity(persona))
                .map(personaEntityMapper::toDomain);
    }

    @Override
    public Mono<Persona> buscarPorId(Long id) {
        return personaRepository.findById(id)
                .map(personaEntityMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existePorCorreo(String correo) {
        return personaRepository.existsByCorreo(correo);
    }

    @Override
    public Flux<Bootcamp> obtenerBootcampsDePersona(Long personaId) {
        return personaBootcampRepository.findByPersonaId(personaId)
                .map(rel -> {
                    Bootcamp b = new Bootcamp();
                    b.setId(rel.getBootcampId());
                    return b;
                });
    }

    @Override
    @Transactional
    public Mono<Void> inscribirEnBootcamp(Long personaId, Long bootcampId) {
        PersonaBootcampEntity entity = new PersonaBootcampEntity(personaId, bootcampId, LocalDate.now());
        return personaBootcampRepository.save(entity).then();
    }

    @Override
    public Mono<Long> contarBootcampsDePersona(Long personaId) {
        return personaBootcampRepository.countByPersonaId(personaId);
    }
}
