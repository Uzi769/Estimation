package ru.irlix.evaluation.service;

import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dto.UserKeycloakDto;

import java.util.UUID;

public interface UserService {
    void createUser(UserKeycloakDto userKeycloakDto);

    void updateUser(User user, UserKeycloakDto userKeycloakDto);

    User findByKeycloakId(UUID keycloakId);

    @Transactional(readOnly = true)
    boolean contains(UUID keycloakId);
}
