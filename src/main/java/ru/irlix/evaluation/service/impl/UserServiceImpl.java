package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dto.UserKeycloakDto;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.UserRepository;
import ru.irlix.evaluation.service.UserService;

import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createUser(UserKeycloakDto userKeycloakDto) {
        User user = User.builder()
                .keycloakId(userKeycloakDto.getId())
                .name(userKeycloakDto.getLastName())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User with id " + savedUser.getUserId() + " saved");
    }

    @Override
    @Transactional
    public void updateUser(User user, UserKeycloakDto userKeycloakDto) {
        if (!user.getName().equals(userKeycloakDto.getLastName()) && userKeycloakDto.getLastName() != null) {
            user.setName(userKeycloakDto.getLastName());
        }
        user.setDeleted(false);
        userRepository.save(user);
        log.info("User with id " + user.getUserId() + " updated");
    }

    @Override
    @Transactional(readOnly = true)
    public User findByKeycloakId(UUID keycloakId) {
        return userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new NotFoundException("User with id " + keycloakId + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean contains(UUID keycloakId) {
        return userRepository.existsByKeycloakId(keycloakId);
    }
}
