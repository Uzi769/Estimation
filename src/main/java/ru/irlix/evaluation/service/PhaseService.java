package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.request.PhaseUpdateRequest;
import ru.irlix.evaluation.dto.response.PhaseStatsResponse;
import ru.irlix.evaluation.dto.response.PhaseResponse;

import java.util.List;

public interface PhaseService {

    PhaseResponse createPhase(PhaseRequest phaseRequest);

    List<PhaseResponse> createPhases(List<PhaseRequest> phaseRequests);

    PhaseResponse updatePhase(Long id, PhaseRequest phaseRequest);

    List<PhaseResponse> updatePhases(List<PhaseUpdateRequest> phaseRequests);

    PhaseResponse findPhaseResponseById(Long id);

    void deletePhase(Long id);

    List<PhaseStatsResponse> getPhaseStats(Long id);
}
