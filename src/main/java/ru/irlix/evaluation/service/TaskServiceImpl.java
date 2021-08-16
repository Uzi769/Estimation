package ru.irlix.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dao.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    @Override
    public void saveTask(Task task) {
        taskRepository.save(task);
    }
}
