package com.onclass.persona.domain.constants;

public class PersonaConstants {
    private PersonaConstants() {}
    public static final int MAX_BOOTCAMPS = 5;
    public static final String BOOTCAMP_BASE_URL = "http://localhost:8082";
    public static final String BOOTCAMP_BUSCAR_ENDPOINT = "/api/v1/bootcamps/{id}";
    public static final String BOOTCAMP_PAGINADO_ENDPOINT = "/api/v1/bootcamps/paginado";
}
