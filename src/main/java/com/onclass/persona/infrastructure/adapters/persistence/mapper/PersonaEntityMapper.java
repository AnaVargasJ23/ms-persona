package com.onclass.persona.infrastructure.adapters.persistence.mapper;

import com.onclass.persona.domain.model.Persona;
import com.onclass.persona.infrastructure.adapters.persistence.entity.PersonaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonaEntityMapper {

    @Mapping(target = "bootcamps", ignore = true)
    Persona toDomain(PersonaEntity entity);

    @Mapping(target = "id", ignore = true)
    PersonaEntity toEntity(Persona persona);
}
