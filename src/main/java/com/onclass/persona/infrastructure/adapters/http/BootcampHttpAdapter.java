package com.onclass.persona.infrastructure.adapters.http;

import com.onclass.persona.domain.constants.PersonaConstants;
import com.onclass.persona.domain.model.Bootcamp;
import com.onclass.persona.domain.spi.IBootcampServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootcampHttpAdapter implements IBootcampServicePort {

    private final WebClient webClient;

    @Override
    public Mono<Bootcamp> obtenerBootcamp(Long id) {
        return webClient.get()
                .uri(PersonaConstants.BOOTCAMP_BASE_URL +
                        PersonaConstants.BOOTCAMP_BUSCAR_ENDPOINT, id)
                .retrieve()
                .bodyToMono(Bootcamp.class)
                .onErrorResume(e -> {
                    log.error("Error obteniendo bootcamp {}: {}", id, e.getMessage());
                    return Mono.empty();
                });
    }
}
