package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dto.UserKeycloakDto;
import ru.irlix.evaluation.service.KeycloakService;
import ru.irlix.evaluation.service.UserService;
import ru.irlix.evaluation.utils.KeycloakProperties;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.client-id}")
    private String client;

    private final UserService userService;
    private final KeycloakProperties keycloakProperties;
    private final Keycloak keycloak;

//    private final String MY_ID = "c8bb7c49-1370-4a2e-a675-aac702817fcf";
//    private final String ADMIN_ID = "3a508839-8bb3-418a-b8a5-cc449004ced6";

    public final Integer KEYCLOAK_FETCH_MAX_VALUE = 1000;

    @Override
    public String getJwt() {
        Keycloak keycloak = this.keycloak(username, password);
        return keycloak.tokenManager().getAccessTokenString();
    }

    private Keycloak keycloak(String username, String password) {
        return keycloakProperties.initializeKeycloakBuilder()
                .username(username)
                .password(password).grantType("password")
                .build();
    }

//    @Override
//    public List<RoleRepresentation> getRoles() {
//        return keycloak.realm(keycloakRealm)
//                .users()
//                .get(ADMIN_ID)
//                .roles()
//                .realmLevel().listEffective();
//    }

    @Override
    public List<UserKeycloakDto> getAllUsers() {
        return keycloak.realm(keycloakRealm).users().search(null, 0, KEYCLOAK_FETCH_MAX_VALUE).parallelStream()
                .map(user -> {
                    String userId = user.getId();
                    return new UserKeycloakDto(UUID.fromString(userId), user.getFirstName(), user.getLastName());
                }).collect(Collectors.toList());
    }

    @Override
    public void update() {
        List<UserKeycloakDto> list = getAllUsers();
        list.forEach(userKeycloakDto -> {
            User user = userService.findByKeycloakId(userKeycloakDto.getId());
            if (user == null)
                userService.createUser(userKeycloakDto);
            else
                userService.updateUser(user, userKeycloakDto);
        });
    }
}
