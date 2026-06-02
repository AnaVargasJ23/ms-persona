package com.onclass.persona.domain.excepcion;

import lombok.Getter;

@Getter
public class PersonaException extends RuntimeException {
    private final String code;
    private final String message;

    public PersonaException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
