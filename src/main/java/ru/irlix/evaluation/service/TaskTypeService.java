package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dto.request.TaskTypeRequest;
import ru.irlix.evaluation.dto.response.TaskTypeResponse;

import java.util.List;

public interface TaskTypeService {

    TaskTypeResponse createTaskType(TaskTypeRequest taskTypeRequest);

    TaskTypeResponse updateTaskType(Long id, TaskTypeRequest taskTypeRequest);

    void deleteTaskType(Long id);

    TaskTypeResponse findTaskTypeResponseById(Long id);

    List<TaskTypeResponse> findAllTaskTypes();
}
