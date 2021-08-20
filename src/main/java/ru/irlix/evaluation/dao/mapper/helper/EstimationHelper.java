package ru.irlix.evaluation.dao.mapper.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.repository.estimation.EstimationRepository;

@Component
@RequiredArgsConstructor
public class EstimationHelper {

    private final EstimationRepository estimationRepository;

    public Estimation findEstimationById(Long id) {
        return estimationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estimation with id " + id + " not found"));
    }
}
