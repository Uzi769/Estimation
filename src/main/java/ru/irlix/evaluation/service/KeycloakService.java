package ru.irlix.evaluation.service;

import org.keycloak.representations.idm.RoleRepresentation;
import ru.irlix.evaluation.dto.UserKeycloakDto;

import java.util.List;

public interface KeycloakService {
    String getJwt();
//    List<RoleRepresentation> getRoles();
    List<UserKeycloakDto> getAllUsers();
}
