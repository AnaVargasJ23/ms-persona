CREATE TABLE IF NOT EXISTS persona (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL,
    correo  VARCHAR(100) NOT NULL UNIQUE,
    edad    INT NOT NULL
);

CREATE TABLE IF NOT EXISTS persona_bootcamp (
    persona_id      BIGINT NOT NULL,
    bootcamp_id     BIGINT NOT NULL,
    fecha_inscripcion DATE NOT NULL,
    PRIMARY KEY (persona_id, bootcamp_id)
);
