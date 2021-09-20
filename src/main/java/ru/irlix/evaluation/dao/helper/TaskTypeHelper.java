package ru.irlix.evaluation.dao.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.TaskTypeDictionary;
import ru.irlix.evaluation.repository.TaskTypeRepository;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class TaskTypeHelper {

    private final TaskTypeRepository taskTypeRepository;

    public TaskTypeDictionary findTypeById(Long id) {
        return taskTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Type with id " + id + " not found"));
    }
}
