package com.onclass.persona.infrastructure.entrypoints;

import com.onclass.persona.domain.api.IPersonaServicePort;
import com.onclass.persona.domain.excepcion.PersonaException;
import com.onclass.persona.domain.model.Persona;
import com.onclass.persona.infrastructure.entrypoints.handler.PersonaHandler;
import com.onclass.persona.infrastructure.entrypoints.mapper.PersonaMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import({PersonaRouter.class, PersonaHandler.class})
class PersonaRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private IPersonaServicePort servicePort;

    @MockBean
    private PersonaMapper personaMapper;

    private com.onclass.persona.infrastructure.entrypoints.dto.PersonaRequest requestValido() {
        return new com.onclass.persona.infrastructure.entrypoints.dto.PersonaRequest(
                "Ana Vargas", "ana@email.com", 25);
    }

    private Persona personaDomain() {
        return new Persona(1L, "Ana Vargas", "ana@email.com", 25, null);
    }

    // ─── POST /api/v1/personas ──────────────────────────────────────────────

    @Test
    void registrar_exitoso_retorna201() {
        when(personaMapper.toDomain(any())).thenReturn(personaDomain());
        when(servicePort.registrar(any())).thenReturn(Mono.just(personaDomain()));

        webTestClient.post()
                .uri("/api/v1/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestValido())
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.nombre").isEqualTo("Ana Vargas")
                .jsonPath("$.correo").isEqualTo("ana@email.com")
                .jsonPath("$.mensaje").isEqualTo("Persona registrada exitosamente");
    }

    @Test
    void registrar_nombreVacio_retorna400() {
        when(personaMapper.toDomain(any())).thenReturn(personaDomain());
        when(servicePort.registrar(any())).thenReturn(
                Mono.error(new PersonaException("PER-001", "El nombre es obligatorio")));

        webTestClient.post()
                .uri("/api/v1/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestValido())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("PER-001");
    }

    @Test
    void registrar_correoVacio_retorna400() {
        when(personaMapper.toDomain(any())).thenReturn(personaDomain());
        when(servicePort.registrar(any())).thenReturn(
                Mono.error(new PersonaException("PER-002", "El correo es obligatorio")));

        webTestClient.post()
                .uri("/api/v1/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestValido())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("PER-002");
    }

    @Test
    void registrar_edadNull_retorna400() {
        when(personaMapper.toDomain(any())).thenReturn(personaDomain());
        when(servicePort.registrar(any())).thenReturn(
                Mono.error(new PersonaException("PER-003", "La edad es obligatoria")));

        webTestClient.post()
                .uri("/api/v1/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestValido())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("PER-003");
    }

    // ─── POST /api/v1/personas/{personaId}/bootcamps/{bootcampId} ───────────

    @Test
    void inscribir_exitoso_retorna200() {
        when(servicePort.inscribirEnBootcamp(1L, 1L)).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/v1/personas/1/bootcamps/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.personaId").isEqualTo(1)
                .jsonPath("$.bootcampId").isEqualTo(1)
                .jsonPath("$.mensaje").isEqualTo("Inscripción exitosa");
    }

    @Test
    void inscribir_personaNoExiste_retorna400() {
        when(servicePort.inscribirEnBootcamp(anyLong(), anyLong())).thenReturn(
                Mono.error(new PersonaException("PER-009", "La persona no existe")));

        webTestClient.post()
                .uri("/api/v1/personas/999/bootcamps/1")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("PER-009");
    }

    @Test
    void inscribir_bootcampNoExiste_retorna400() {
        when(servicePort.inscribirEnBootcamp(anyLong(), anyLong())).thenReturn(
                Mono.error(new PersonaException("PER-006", "El bootcamp no existe")));

        webTestClient.post()
                .uri("/api/v1/personas/1/bootcamps/999")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("PER-006");
    }

    @Test
    void inscribir_maxBootcamps_retorna400() {
        when(servicePort.inscribirEnBootcamp(anyLong(), anyLong())).thenReturn(
                Mono.error(new PersonaException("PER-005",
                        "Una persona no puede inscribirse en más de 5 bootcamps")));

        webTestClient.post()
                .uri("/api/v1/personas/1/bootcamps/2")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("PER-005");
    }

    @Test
    void inscribir_yaInscrito_retorna400() {
        when(servicePort.inscribirEnBootcamp(anyLong(), anyLong())).thenReturn(
                Mono.error(new PersonaException("PER-007",
                        "La persona ya está inscrita en este bootcamp")));

        webTestClient.post()
                .uri("/api/v1/personas/1/bootcamps/1")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("PER-007");
    }

    @Test
    void inscribir_solapamientoFechas_retorna400() {
        when(servicePort.inscribirEnBootcamp(anyLong(), anyLong())).thenReturn(
                Mono.error(new PersonaException("PER-008",
                        "El bootcamp se solapa con otro bootcamp en el que ya está inscrito")));

        webTestClient.post()
                .uri("/api/v1/personas/1/bootcamps/3")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.code").isEqualTo("PER-008");
    }
}