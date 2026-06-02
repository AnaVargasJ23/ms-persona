package com.onclass.persona.domain.usecase;

import com.onclass.persona.domain.api.IPersonaServicePort;
import com.onclass.persona.domain.constants.PersonaConstants;
import com.onclass.persona.domain.enums.PersonaErrorEnum;
import com.onclass.persona.domain.excepcion.PersonaException;
import com.onclass.persona.domain.model.Bootcamp;
import com.onclass.persona.domain.model.Persona;
import com.onclass.persona.domain.spi.IBootcampServicePort;
import com.onclass.persona.domain.spi.IPersonaPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RequiredArgsConstructor
public class PersonaUseCase implements IPersonaServicePort {

    private final IPersonaPersistencePort persistencePort;
    private final IBootcampServicePort bootcampServicePort;

    @Override
    public Mono<Persona> registrar(Persona persona) {
        if (persona.getNombre() == null || persona.getNombre().isBlank()) {
            return Mono.error(new PersonaException(
                    PersonaErrorEnum.NOMBRE_OBLIGATORIO.getCode(),
                    PersonaErrorEnum.NOMBRE_OBLIGATORIO.getMessage()));
        }
        if (persona.getCorreo() == null || persona.getCorreo().isBlank()) {
            return Mono.error(new PersonaException(
                    PersonaErrorEnum.CORREO_OBLIGATORIO.getCode(),
                    PersonaErrorEnum.CORREO_OBLIGATORIO.getMessage()));
        }
        if (persona.getEdad() == null) {
            return Mono.error(new PersonaException(
                    PersonaErrorEnum.EDAD_OBLIGATORIA.getCode(),
                    PersonaErrorEnum.EDAD_OBLIGATORIA.getMessage()));
        }
        return persistencePort.guardar(persona);
    }

    @Override
    public Mono<Void> inscribirEnBootcamp(Long personaId, Long bootcampId) {
        return persistencePort.buscarPorId(personaId)
                .switchIfEmpty(Mono.error(new PersonaException(
                        PersonaErrorEnum.PERSONA_NO_ENCONTRADA.getCode(),
                        PersonaErrorEnum.PERSONA_NO_ENCONTRADA.getMessage())))
                .flatMap(persona -> bootcampServicePort.obtenerBootcamp(bootcampId)
                        .switchIfEmpty(Mono.error(new PersonaException(
                                PersonaErrorEnum.BOOTCAMP_NO_EXISTE.getCode(),
                                PersonaErrorEnum.BOOTCAMP_NO_EXISTE.getMessage())))
                        .flatMap(bootcampNuevo ->
                                persistencePort.contarBootcampsDePersona(personaId)
                                        .flatMap(cantidad -> {
                                            if (cantidad >= PersonaConstants.MAX_BOOTCAMPS) {
                                                return Mono.error(new PersonaException(
                                                        PersonaErrorEnum.MAX_BOOTCAMPS.getCode(),
                                                        PersonaErrorEnum.MAX_BOOTCAMPS.getMessage()));
                                            }
                                            return persistencePort.obtenerBootcampsDePersona(personaId)
                                                    .collectList()
                                                    .flatMap(idsBootcamps -> 
                                                        Flux.fromIterable(idsBootcamps)
                                                            .flatMap(b -> bootcampServicePort.obtenerBootcamp(b.getId()))
                                                            .collectList()
                                                            .flatMap(bootcampsActuales -> {
                                                                boolean yaInscrito = bootcampsActuales.stream()
                                                                        .anyMatch(b -> b.getId().equals(bootcampId));
                                                                if (yaInscrito) {
                                                                    return Mono.error(new PersonaException(
                                                                            PersonaErrorEnum.BOOTCAMP_YA_INSCRITO.getCode(),
                                                                            PersonaErrorEnum.BOOTCAMP_YA_INSCRITO.getMessage()));
                                                                }
                                                                boolean seSolapa = bootcampsActuales.stream()
                                                                        .anyMatch(b -> seSolapa(b, bootcampNuevo));
                                                                if (seSolapa) {
                                                                    return Mono.error(new PersonaException(
                                                                            PersonaErrorEnum.SOLAPAMIENTO_FECHAS.getCode(),
                                                                            PersonaErrorEnum.SOLAPAMIENTO_FECHAS.getMessage()));
                                                                }
                                                                return persistencePort.inscribirEnBootcamp(personaId, bootcampId);
                                                            })
                                                    );
                                        })
                        )
                );
    }

    private boolean seSolapa(Bootcamp existente, Bootcamp nuevo) {
        LocalDate inicioExistente = existente.getFechaLanzamiento();
        LocalDate finExistente = inicioExistente.plusDays(existente.getDuracion());
        LocalDate inicioNuevo = nuevo.getFechaLanzamiento();
        LocalDate finNuevo = inicioNuevo.plusDays(nuevo.getDuracion());
        return inicioNuevo.isBefore(finExistente) && finNuevo.isAfter(inicioExistente);
    }
}
