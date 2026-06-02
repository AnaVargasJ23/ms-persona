package com.onclass.persona.infrastructure.adapters.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("persona")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonaEntity {
    @Id
    private Long id;
    private String nombre;
    private String correo;
    private Integer edad;
}
