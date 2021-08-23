package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dto.request.TaskRequest;
import ru.irlix.evaluation.dto.response.TaskResponse;

import java.util.Set;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);

    TaskResponse updateTask(Long id, TaskRequest taskRequest);

    TaskResponse findTaskResponseById(Long id);

    Set<TaskResponse> findTasks(Long phaseId);

    void deleteTask(Long id);
}
