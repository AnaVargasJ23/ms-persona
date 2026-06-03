package com.onclass.persona.domain.usecase;

import com.onclass.persona.domain.excepcion.PersonaException;
import com.onclass.persona.domain.model.Bootcamp;
import com.onclass.persona.domain.model.Persona;
import com.onclass.persona.domain.spi.IBootcampServicePort;
import com.onclass.persona.domain.spi.IPersonaPersistencePort;
import com.onclass.persona.domain.spi.IReporteServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonaUseCaseTest {

    @Mock
    private IPersonaPersistencePort persistencePort;

    @Mock
    private IBootcampServicePort bootcampServicePort;

    @Mock
    private IReporteServicePort reporteServicePort;

    @InjectMocks
    private PersonaUseCase useCase;

    private Persona personaValida() {
        return new Persona(null, "Ana Vargas", "ana@email.com", 25, null);
    }

    private Bootcamp bootcampSinSolapamiento() {
        return new Bootcamp(1L, "Bootcamp Java", LocalDate.of(2028, 1, 1), 30);
    }

    @Test
    void registrar_exitoso() {
        when(persistencePort.guardar(any())).thenReturn(Mono.just(
                new Persona(1L, "Ana Vargas", "ana@email.com", 25, null)));

        StepVerifier.create(useCase.registrar(personaValida()))
                .expectNextMatches(p -> p.getId() == 1L)
                .verifyComplete();
    }

    @Test
    void registrar_nombreVacio_lanzaError() {
        Persona persona = new Persona(null, "", "ana@email.com", 25, null);

        StepVerifier.create(useCase.registrar(persona))
                .expectError(PersonaException.class)
                .verify();
    }

    @Test
    void registrar_nombreNull_lanzaError() {
        Persona persona = new Persona(null, null, "ana@email.com", 25, null);

        StepVerifier.create(useCase.registrar(persona))
                .expectError(PersonaException.class)
                .verify();
    }

    @Test
    void registrar_correoVacio_lanzaError() {
        Persona persona = new Persona(null, "Ana", "", 25, null);

        StepVerifier.create(useCase.registrar(persona))
                .expectError(PersonaException.class)
                .verify();
    }

    @Test
    void registrar_correoNull_lanzaError() {
        Persona persona = new Persona(null, "Ana", null, 25, null);

        StepVerifier.create(useCase.registrar(persona))
                .expectError(PersonaException.class)
                .verify();
    }

    @Test
    void registrar_edadNull_lanzaError() {
        Persona persona = new Persona(null, "Ana", "ana@email.com", null, null);

        StepVerifier.create(useCase.registrar(persona))
                .expectError(PersonaException.class)
                .verify();
    }

    @Test
    void inscribir_exitoso() {
        Bootcamp bootcamp = bootcampSinSolapamiento();
        when(persistencePort.buscarPorId(1L)).thenReturn(Mono.just(personaValida()));
        when(bootcampServicePort.obtenerBootcamp(1L)).thenReturn(Mono.just(bootcamp));
        when(persistencePort.contarBootcampsDePersona(1L)).thenReturn(Mono.just(0L));
        when(persistencePort.obtenerBootcampsDePersona(1L)).thenReturn(Flux.empty());
        when(persistencePort.inscribirEnBootcamp(1L, 1L)).thenReturn(Mono.empty());
        when(persistencePort.contarBootcampsDePersona(1L)).thenReturn(Mono.just(1L));
        doNothing().when(reporteServicePort).actualizarPersonas(anyLong(), anyLong());

        StepVerifier.create(useCase.inscribirEnBootcamp(1L, 1L))
                .verifyComplete();
    }

    @Test
    void inscribir_personaNoExiste_lanzaError() {
        when(persistencePort.buscarPorId(999L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.inscribirEnBootcamp(999L, 1L))
                .expectError(PersonaException.class)
                .verify();
    }

    @Test
    void inscribir_bootcampNoExiste_lanzaError() {
        when(persistencePort.buscarPorId(1L)).thenReturn(Mono.just(personaValida()));
        when(bootcampServicePort.obtenerBootcamp(999L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.inscribirEnBootcamp(1L, 999L))
                .expectError(PersonaException.class)
                .verify();
    }

    @Test
    void inscribir_maxBootcamps_lanzaError() {
        when(persistencePort.buscarPorId(1L)).thenReturn(Mono.just(personaValida()));
        when(bootcampServicePort.obtenerBootcamp(1L)).thenReturn(Mono.just(bootcampSinSolapamiento()));
        when(persistencePort.contarBootcampsDePersona(1L)).thenReturn(Mono.just(5L));

        StepVerifier.create(useCase.inscribirEnBootcamp(1L, 1L))
                .expectError(PersonaException.class)
                .verify();
    }

    @Test
    void inscribir_yaInscrito_lanzaError() {
        Bootcamp bootcamp = bootcampSinSolapamiento();
        when(persistencePort.buscarPorId(1L)).thenReturn(Mono.just(personaValida()));
        when(bootcampServicePort.obtenerBootcamp(1L)).thenReturn(Mono.just(bootcamp));
        when(persistencePort.contarBootcampsDePersona(1L)).thenReturn(Mono.just(1L));
        when(persistencePort.obtenerBootcampsDePersona(1L)).thenReturn(Flux.just(bootcamp));
        when(bootcampServicePort.obtenerBootcamp(1L)).thenReturn(Mono.just(bootcamp));

        StepVerifier.create(useCase.inscribirEnBootcamp(1L, 1L))
                .expectError(PersonaException.class)
                .verify();
    }

    @Test
    void inscribir_solapamientoFechas_lanzaError() {
        Bootcamp bootcampExistente = new Bootcamp(2L, "Bootcamp Existente",
                LocalDate.of(2028, 1, 1), 30);
        Bootcamp bootcampNuevo = new Bootcamp(3L, "Bootcamp Nuevo",
                LocalDate.of(2028, 1, 15), 30);

        when(persistencePort.buscarPorId(1L)).thenReturn(Mono.just(personaValida()));
        when(bootcampServicePort.obtenerBootcamp(3L)).thenReturn(Mono.just(bootcampNuevo));
        when(persistencePort.contarBootcampsDePersona(1L)).thenReturn(Mono.just(1L));
        when(persistencePort.obtenerBootcampsDePersona(1L)).thenReturn(Flux.just(bootcampExistente));
        when(bootcampServicePort.obtenerBootcamp(2L)).thenReturn(Mono.just(bootcampExistente));

        StepVerifier.create(useCase.inscribirEnBootcamp(1L, 3L))
                .expectError(PersonaException.class)
                .verify();
    }
}
