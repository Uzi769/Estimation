package ru.irlix.evaluation.dao.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dao.mapper.UserMapper;
import ru.irlix.evaluation.dto.UserKeycloakDto;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class UserHelper {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public User findUserByKeycloakId(String keycloakId) {
        UUID keycloakUuid = UUID.fromString(keycloakId);
        return userRepository.findByKeycloakId(keycloakUuid)
                .orElseThrow(() -> new NotFoundException("User with id " + keycloakId + " not found"));
    }

    public List<User> findByUserIdIn(List<Long> userIds) {
        return userRepository.findByUserIdIn(userIds);
    }

    public void createUser(UserKeycloakDto userKeycloakDto) {
        User savedUser = userRepository.save(mapper.userKeycloakDtoToUser(userKeycloakDto));
        log.info("User with id " + savedUser.getUserId() + " saved");
    }

    public void updateUser(User user, UserKeycloakDto userKeycloakDto) {
        if (!Objects.equals(user.getFirstName(), userKeycloakDto.getFirstName()) && userKeycloakDto.getFirstName() != null) {
            user.setFirstName(userKeycloakDto.getFirstName());
        }
        if (!Objects.equals(user.getLastName(), userKeycloakDto.getLastName()) && userKeycloakDto.getLastName() != null) {
            user.setLastName(userKeycloakDto.getLastName());
        }
        user.setDeleted(false);
        userRepository.save(user);
        log.info("User with id " + user.getUserId() + " updated");
    }

    public boolean contains(UUID keycloakId) {
        return userRepository.existsByKeycloakId(keycloakId);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.save(user);
        log.info("User with id " + user.getUserId() + " updated");
    }
}
