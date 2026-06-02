package com.onclass.persona.infrastructure.adapters.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("persona_bootcamp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonaBootcampEntity {
    private Long personaId;
    private Long bootcampId;
    private LocalDate fechaInscripcion;
}
