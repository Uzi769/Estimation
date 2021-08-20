package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;

import java.util.Set;

public interface PhaseService {
    PhaseResponse createPhase(PhaseRequest phaseRequest);

    PhaseResponse updatePhase(Long id, PhaseRequest phaseRequest);

    PhaseResponse findPhaseResponseById(Long id);

    void deletePhase(Long id);

    Set<PhaseResponse> getPhaseSetByEstimationId(Long id);
}
