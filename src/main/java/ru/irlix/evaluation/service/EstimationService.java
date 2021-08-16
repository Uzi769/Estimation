package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dao.dto.request.EstimationRequest;
import ru.irlix.evaluation.dao.dto.response.EstimationResponse;

public interface EstimationService {
    EstimationResponse createEstimation(EstimationRequest estimationRequest);
}
