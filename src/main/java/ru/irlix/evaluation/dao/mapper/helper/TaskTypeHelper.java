package ru.irlix.evaluation.dao.mapper.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.TaskTypeDictionary;
import ru.irlix.evaluation.repository.TaskTypeRepository;

@Log4j2
@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class TaskTypeHelper {

    private final TaskTypeRepository taskTypeRepository;

    public TaskTypeDictionary findTypeById(Long id) {
        return taskTypeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Method findTypeById: Type with id " + id + " not found");
                    return new NotFoundException("Type with id " + id + " not found");
                });
    }
}
