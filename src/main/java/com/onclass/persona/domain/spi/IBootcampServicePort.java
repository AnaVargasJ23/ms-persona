package com.onclass.persona.domain.spi;

import com.onclass.persona.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface IBootcampServicePort {
    Mono<Bootcamp> obtenerBootcamp(Long id);
}
