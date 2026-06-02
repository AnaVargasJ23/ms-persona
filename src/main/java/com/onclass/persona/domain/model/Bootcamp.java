package com.onclass.persona.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bootcamp {
    private Long id;
    private String nombre;
    private LocalDate fechaLanzamiento;
    private Integer duracion;
}
