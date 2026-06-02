package com.onclass.persona.infrastructure.entrypoints.mapper;

import com.onclass.persona.domain.model.Persona;
import com.onclass.persona.infrastructure.entrypoints.dto.PersonaRequest;
import com.onclass.persona.infrastructure.entrypoints.dto.PersonaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bootcamps", ignore = true)
    Persona toDomain(PersonaRequest request);

    PersonaResponse toResponse(Persona persona);
}
