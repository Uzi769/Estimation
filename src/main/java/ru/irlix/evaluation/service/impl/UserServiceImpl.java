package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dao.helper.UserHelper;
import ru.irlix.evaluation.dao.mapper.UserMapper;
import ru.irlix.evaluation.dto.UserKeycloakDto;
import ru.irlix.evaluation.dto.response.UserResponse;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.UserRepository;
import ru.irlix.evaluation.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final UserHelper userHelper;

    @Override
    @Transactional
    public void createUser(UserKeycloakDto userKeycloakDto) {
        User savedUser = userRepository.save(mapper.userKeycloakDtoToUser(userKeycloakDto));
        log.info("User with id " + savedUser.getUserId() + " saved");
    }

    @Override
    @Transactional
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

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAllUsers() {
        List<User> userList = userRepository.findAll();
        log.info("All users found");
        return mapper.usersToUserResponseList(userList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userHelper.findAllUsers();
    }
}
