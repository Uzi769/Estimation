package ru.irlix.evaluation.config;

import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.irlix.evaluation.utils.security.KeycloakProperties;

@Configuration
@EnableScheduling
public class KeycloakConfig {

    @Bean
    @ConfigurationProperties(prefix = "keycloak")
    public KeycloakProperties keycloakProperties() {
        return new KeycloakProperties();
    }

    @Bean
    public Keycloak keycloak() {
        return keycloakProperties().initializeKeycloakBuilder().build();
    }
}
