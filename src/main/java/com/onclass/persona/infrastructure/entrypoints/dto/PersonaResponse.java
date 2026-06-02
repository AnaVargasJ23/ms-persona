package com.onclass.persona.infrastructure.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonaResponse {
    private Long id;
    private String nombre;
    private String correo;
    private Integer edad;
}
