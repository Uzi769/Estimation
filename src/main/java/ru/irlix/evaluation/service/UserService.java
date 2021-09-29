package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAllUsers();
}
