package ru.irlix.evaluation.service.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.dto.request.TaskRequest;
import ru.irlix.evaluation.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public void saveTask(TaskRequest taskRequest) {

    }
}
