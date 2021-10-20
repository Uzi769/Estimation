package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dto.response.ActionResponse;

import java.util.List;

public interface ActionService {

    List<ActionResponse> findAllActions();
}
