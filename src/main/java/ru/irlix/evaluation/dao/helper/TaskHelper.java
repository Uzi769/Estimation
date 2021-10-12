package ru.irlix.evaluation.dao.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.repository.TaskRepository;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class TaskHelper {

    private final TaskRepository taskRepository;

    public Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task with id " + id + " not found"));
    }

    public Task findTaskByName(String name) {
        return taskRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Task with id " + name + " not found"));
    }
}
