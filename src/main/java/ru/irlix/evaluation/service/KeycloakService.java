package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dto.UserKeycloakDto;

import java.util.List;

public interface KeycloakService {

    String getJwt();

    List<UserKeycloakDto> getAllUsers();

    void update();
}
