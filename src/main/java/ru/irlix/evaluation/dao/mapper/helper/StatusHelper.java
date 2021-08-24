package ru.irlix.evaluation.dao.mapper.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.repository.StatusRepository;

@Log4j2
@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class StatusHelper {

    private final StatusRepository statusRepository;

    public Status findStatusById(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Method findStatusById: Status with id " + id + " not found");
                    return new NotFoundException("Status with id " + id + " not found");
                });
    }
}
