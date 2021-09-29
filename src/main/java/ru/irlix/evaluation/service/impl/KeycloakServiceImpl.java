package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dto.UserKeycloakDto;
import ru.irlix.evaluation.service.KeycloakService;
import ru.irlix.evaluation.service.UserService;
import ru.irlix.evaluation.utils.security.KeycloakProperties;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final UserService userService;
    private final KeycloakProperties keycloakProperties;
    private final Keycloak keycloak;

    public final Integer KEYCLOAK_FETCH_MAX_VALUE = 1000;

    @Override
    public String getJwt() {
        return keycloak.tokenManager().getAccessTokenString();
    }

    @Override
    public List<UserKeycloakDto> getAllUsers() {
        return keycloak.realm(keycloakProperties.getRealm()).users().search(null, 0, KEYCLOAK_FETCH_MAX_VALUE)
                .parallelStream()
                .map(user -> new UserKeycloakDto(UUID.fromString(user.getId()), user.getFirstName(), user.getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    public void update() {
        List<UserKeycloakDto> userList = getAllUsers();
        userList.forEach(userKeycloakDto -> {
            boolean isExist = userService.contains(userKeycloakDto.getId());
            if (!isExist) {
                userService.createUser(userKeycloakDto);
            } else {
                User user = userService.findByKeycloakId(userKeycloakDto.getId());
                userService.updateUser(user, userKeycloakDto);
            }
        });

        List<UUID> userKeycloakIdList = userList.stream()
                .map(UserKeycloakDto::getId)
                .collect(Collectors.toList());

        userService.findAll().stream()
                .filter(user -> !userKeycloakIdList.contains(user.getKeycloakId()))
                .forEach(user -> user.setDeleted(true));
    }
}
