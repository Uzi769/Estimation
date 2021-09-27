package ru.irlix.evaluation.dao.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.repository.StatusRepository;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class StatusHelper {

    private final StatusRepository statusRepository;

    public Status findStatusById(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Status with id " + id + " not found"));
    }
}
