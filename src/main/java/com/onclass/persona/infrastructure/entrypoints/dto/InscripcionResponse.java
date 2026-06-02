package com.onclass.persona.infrastructure.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionResponse {
    private Long personaId;
    private Long bootcampId;
    private String mensaje;
}
