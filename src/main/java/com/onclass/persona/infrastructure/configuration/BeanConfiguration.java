package com.onclass.persona.infrastructure.configuration;

import com.onclass.persona.domain.api.IPersonaServicePort;
import com.onclass.persona.domain.constants.PersonaConstants;
import com.onclass.persona.domain.spi.IBootcampServicePort;
import com.onclass.persona.domain.spi.IPersonaPersistencePort;
import com.onclass.persona.domain.spi.IReporteServicePort;
import com.onclass.persona.domain.usecase.PersonaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(PersonaConstants.BOOTCAMP_BASE_URL)
                .build();
    }

    @Bean
    public IPersonaServicePort personaServicePort(
            IPersonaPersistencePort persistencePort,
            IBootcampServicePort bootcampServicePort,
            IReporteServicePort reporteServicePort) {
        return new PersonaUseCase(persistencePort, bootcampServicePort, reporteServicePort);
    }
}
