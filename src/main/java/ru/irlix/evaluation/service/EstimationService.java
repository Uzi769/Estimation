package ru.irlix.evaluation.service;

import org.springframework.core.io.Resource;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.dto.response.PhaseResponse;

import java.io.IOException;
import java.util.List;

public interface EstimationService {

    EstimationResponse createEstimation(EstimationRequest estimationRequest);

    EstimationResponse updateEstimation(Long id, EstimationRequest request);

    void deleteEstimation(Long id);

    List<EstimationResponse> findAllEstimations(EstimationFilterRequest request);

    EstimationResponse findEstimationResponseById(Long id);

    List<PhaseResponse> findPhaseResponsesByEstimationId(Long id);

    Resource getEstimationsReport() throws IOException;
}
