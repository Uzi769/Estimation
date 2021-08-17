package ru.irlix.evaluation.service.estimation;

import org.springframework.data.domain.Pageable;
import ru.irlix.evaluation.dao.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dao.dto.request.EstimationRequest;
import ru.irlix.evaluation.dao.dto.response.EstimationResponse;

import java.util.List;

public interface EstimationService {
    Long createEstimation(EstimationRequest estimationRequest);

    EstimationResponse updateEstimation(Long id, EstimationRequest request);

    void deleteEstimation(Long id);

    List<EstimationResponse> findAllEstimations(EstimationFilterRequest request);
}
