package ru.irlix.evaluation.utils.security;

import lombok.Getter;
import lombok.Setter;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.KeycloakBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class KeycloakProperties {

    private String serverUrl;
    private String realm;
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;

    public KeycloakBuilder initializeKeycloakBuilder() {

        KeycloakBuilder keycloakBuild = KeycloakBuilder.builder();

        if (serverUrl != null) {
            keycloakBuild.serverUrl(serverUrl);
        }

        if (realm != null) {
            keycloakBuild.realm(realm);
        }

        if (clientId != null) {
            keycloakBuild.clientId(clientId);
        }

        if (clientSecret != null) {
            keycloakBuild.clientSecret(clientSecret);
        }

        if (username != null) {
            keycloakBuild.username(username);
        }

        if (password != null) {
            keycloakBuild.password(password).grantType("password");
        }

        keycloakBuild.resteasyClient(new ResteasyClientBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .connectionPoolSize(50)
                .asyncExecutor(Executors.newFixedThreadPool(100))
                .hostnameVerification(ResteasyClientBuilder.HostnameVerificationPolicy.ANY)
                .build());

        return keycloakBuild;
    }
}
