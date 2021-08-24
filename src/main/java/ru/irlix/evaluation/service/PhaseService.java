package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;

import java.util.List;

public interface PhaseService {
    PhaseResponse createPhase(PhaseRequest phaseRequest);

    PhaseResponse updatePhase(Long id, PhaseRequest phaseRequest);

    PhaseResponse findPhaseResponseById(Long id);

    void deletePhase(Long id);

    List<PhaseResponse> findPhasesByEstimationId(Long id);
}
