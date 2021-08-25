package ru.irlix.evaluation.repository.estimation;

import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;

import java.util.List;

public interface EstimationFilterRepository {

    List<Estimation> filter(EstimationFilterRequest request);
}
