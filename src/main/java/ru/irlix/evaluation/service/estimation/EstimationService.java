package ru.irlix.evaluation.service.estimation;

import ru.irlix.evaluation.dao.dto.request.EstimationRequest;
import ru.irlix.evaluation.dao.dto.response.EstimationResponse;

public interface EstimationService {
    EstimationResponse createEstimation(EstimationRequest estimationRequest);
}
