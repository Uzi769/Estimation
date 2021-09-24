package ru.irlix.evaluation.dao.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.UserRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class UserHelper {

    private final UserRepository userRepository;

    public User findUserByKeycloakId(String keycloakId) {
        UUID keycloakUuid = UUID.fromString(keycloakId);
        return userRepository.findByKeycloakId(keycloakUuid)
                .orElseThrow(() -> new NotFoundException("User with id " + keycloakId + " not found"));
    }
}
