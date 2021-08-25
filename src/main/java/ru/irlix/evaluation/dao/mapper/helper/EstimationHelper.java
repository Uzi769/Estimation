package ru.irlix.evaluation.dao.mapper.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.repository.estimation.EstimationRepository;

@Log4j2
@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class EstimationHelper {

    private final EstimationRepository estimationRepository;

    public Estimation findEstimationById(Long id) {
        return estimationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estimation with id " + id + " not found"));
    }
}
