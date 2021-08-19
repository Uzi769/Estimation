package ru.irlix.evaluation.service.task;

import ru.irlix.evaluation.dto.request.TaskRequest;

public interface TaskService {
    void createTask(TaskRequest taskRequest);
}
