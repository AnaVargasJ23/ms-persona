package com.onclass.persona.infrastructure.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonaRegistradaResponse {
    private Long id;
    private String nombre;
    private String correo;
    private String mensaje;
}
