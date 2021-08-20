package ru.irlix.evaluation.service.estimation;

import ru.irlix.evaluation.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;

import java.util.List;

public interface EstimationService {
    EstimationResponse createEstimation(EstimationRequest estimationRequest);

    EstimationResponse updateEstimation(Long id, EstimationRequest request);

    void deleteEstimation(Long id);

    List<EstimationResponse> findAllEstimations(EstimationFilterRequest request);

    EstimationResponse findEstimationResponseById(Long id);
}
