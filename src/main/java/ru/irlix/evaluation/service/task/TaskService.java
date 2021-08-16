package ru.irlix.evaluation.service.task;

import ru.irlix.evaluation.dao.dto.request.TaskRequest;

public interface TaskService {
    void saveTask(TaskRequest taskRequest);
}
