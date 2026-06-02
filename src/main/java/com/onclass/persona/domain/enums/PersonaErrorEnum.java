package com.onclass.persona.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PersonaErrorEnum {

    NOMBRE_OBLIGATORIO("PER-001", "El nombre es obligatorio"),
    CORREO_OBLIGATORIO("PER-002", "El correo es obligatorio"),
    EDAD_OBLIGATORIA("PER-003", "La edad es obligatoria"),
    BOOTCAMP_OBLIGATORIO("PER-004", "El bootcamp es obligatorio"),
    MAX_BOOTCAMPS("PER-005", "Una persona no puede inscribirse en más de 5 bootcamps"),
    BOOTCAMP_NO_EXISTE("PER-006", "El bootcamp no existe"),
    BOOTCAMP_YA_INSCRITO("PER-007", "La persona ya está inscrita en este bootcamp"),
    SOLAPAMIENTO_FECHAS("PER-008", "El bootcamp se solapa con otro bootcamp en el que ya está inscrito"),
    PERSONA_NO_ENCONTRADA("PER-009", "La persona no existe");

    private final String code;
    private final String message;
}
