package ru.irlix.evaluation.dao.mapper.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
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
}
