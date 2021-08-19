package ru.irlix.evaluation.service.estimation;

import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;

import java.util.List;

public interface EstimationService {
    EstimationResponse createEstimation(EstimationRequest estimationRequest);

    List<EstimationResponse> findAllEstimations();
}
