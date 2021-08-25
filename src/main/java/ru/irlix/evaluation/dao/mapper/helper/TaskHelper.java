package ru.irlix.evaluation.dao.mapper.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.repository.TaskRepository;

@Log4j2
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
