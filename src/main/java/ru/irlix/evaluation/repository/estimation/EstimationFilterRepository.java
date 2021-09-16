package ru.irlix.evaluation.repository.estimation;

import org.springframework.data.domain.Page;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dto.request.EstimationFindAnyRequest;

public interface EstimationFilterRepository {

    Page<Estimation> filter(EstimationFilterRequest request);

    Page<Estimation> findAny(EstimationFindAnyRequest request);
}
