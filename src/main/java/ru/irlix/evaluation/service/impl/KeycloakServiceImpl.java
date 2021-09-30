package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dao.helper.UserHelper;
import ru.irlix.evaluation.dto.UserKeycloakDto;
import ru.irlix.evaluation.service.KeycloakService;
import ru.irlix.evaluation.utils.security.KeycloakProperties;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final KeycloakProperties keycloakProperties;
    private final Keycloak keycloak;
    private final UserHelper userHelper;

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

    @Transactional
    @Override
    public void update() {
        List<UserKeycloakDto> keycloakUserList = getAllUsers();

        List<User> userList = userHelper.findAllUsers();

        List<UUID> userKeycloakIdList = userList.stream()
                .map(User::getKeycloakId)
                .collect(Collectors.toList());

        keycloakUserList.forEach(userKeycloakDto -> {
            boolean isExist = userKeycloakIdList.contains(userKeycloakDto.getId());
            if (!isExist) {
                userHelper.createUser(userKeycloakDto);
            } else {
                User user = userHelper.findUserByKeycloakId(userKeycloakDto.getId().toString());
                userHelper.updateUser(user, userKeycloakDto);
            }
        });

        userList.stream()
                .filter(user -> !userKeycloakIdList.contains(user.getKeycloakId()))
                .forEach(user -> user.setDeleted(true));
        userHelper.saveUsers(userList);
    }
}
