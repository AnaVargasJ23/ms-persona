package com.onclass.persona.infrastructure.adapters.persistence.repository;

import com.onclass.persona.infrastructure.adapters.persistence.entity.PersonaBootcampEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonaBootcampR2dbcRepository extends ReactiveCrudRepository<PersonaBootcampEntity, Long> {
    Flux<PersonaBootcampEntity> findByPersonaId(Long personaId);
    Mono<Long> countByPersonaId(Long personaId);

    @Query("DELETE FROM persona_bootcamp WHERE persona_id = :personaId AND bootcamp_id = :bootcampId")
    Mono<Void> deleteByPersonaIdAndBootcampId(Long personaId, Long bootcampId);
}
