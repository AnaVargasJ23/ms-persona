package com.onclass.persona.infrastructure.adapters.http;

import com.onclass.persona.domain.spi.IReporteServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReporteHttpAdapter implements IReporteServicePort {

    private final WebClient webClient;

    @Override
    public void actualizarPersonas(Long bootcampId, Long cantidad) {
        webClient.put()
                .uri("http://localhost:8084/api/v1/reportes/{bootcampId}/personas/{cantidad}",
                        bootcampId, cantidad)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(r -> log.info("Reporte actualizado bootcamp {}: {} personas", bootcampId, cantidad))
                .doOnError(e -> log.error("Error actualizando reporte: {}", e.getMessage()))
                .subscribe();
    }
}
