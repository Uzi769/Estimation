package ru.irlix.evaluation.service.estimation;

import ru.irlix.evaluation.dao.dto.request.EstimationRequest;
import ru.irlix.evaluation.dao.dto.response.EstimationResponse;

import java.util.List;

public interface EstimationService {
    EstimationResponse createOrUpdateEstimation(EstimationRequest estimationRequest);
    EstimationResponse findEstimationById(Long id);
    List<EstimationResponse> findAll();
    void deleteEstimationById(Long id);
}
