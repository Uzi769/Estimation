package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;

import java.io.IOException;
import java.util.List;

public interface PhaseService {

    PhaseResponse createPhase(PhaseRequest phaseRequest);

    List<PhaseResponse> createPhases(List<PhaseRequest> phaseRequests);

    PhaseResponse updatePhase(Long id, PhaseRequest phaseRequest);

    PhaseResponse findPhaseResponseById(Long id);

    void deletePhase(Long id);

    void unloadingPhases() throws IOException;
}
