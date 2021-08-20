package ru.irlix.evaluation.dao.mapper.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.TaskTypeDictionary;
import ru.irlix.evaluation.repository.TaskTypeRepository;

@Component
@RequiredArgsConstructor
public class TaskTypeHelper {

    private final TaskTypeRepository taskTypeRepository;

    public TaskTypeDictionary findTypeById(Long id) {
        return taskTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Type with id " + id + " not found"));
    }
}
