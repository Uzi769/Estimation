package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dao.mapper.UserMapper;
import ru.irlix.evaluation.dto.response.UserResponse;
import ru.irlix.evaluation.repository.UserRepository;
import ru.irlix.evaluation.service.UserService;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAllUsers() {
        List<User> userList = userRepository.findAll();
        log.info("All users found");
        return mapper.usersToUserResponseList(userList);
    }

}
