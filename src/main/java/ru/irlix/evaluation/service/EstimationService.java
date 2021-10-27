package ru.irlix.evaluation.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface EstimationService {

    EstimationResponse createEstimation(EstimationRequest estimationRequest);

    EstimationResponse updateEstimation(Long id, EstimationRequest request);

    void setEstimationDeleted(Long id);

    void deleteEstimation(Long id);

    Page<EstimationResponse> filterEstimations(EstimationFilterRequest request);

    EstimationResponse findEstimationResponseById(Long id);

    List<PhaseResponse> findPhaseResponsesByEstimationId(Long id);

    EstimationResponse restoreEstimation(Long id);

    Resource getEstimationsReport(Long id, Map<String, String> request) throws IOException;

    List<EstimationStatsResponse> getEstimationStats(Long id);

    EstimationCostResponse getEstimationCost(Long id, Map<String, String> request);

    List<FileStorageResponse> findFileResponsesByEstimationId(Long id);
}
