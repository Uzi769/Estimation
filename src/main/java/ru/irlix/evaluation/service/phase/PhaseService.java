package ru.irlix.evaluation.service.phase;

import ru.irlix.evaluation.dao.dto.request.PhaseRequest;
import ru.irlix.evaluation.dao.entity.Phase;

import java.util.List;

public interface PhaseService {
    void savePhases(List<PhaseRequest> phaseRequestList);
    void savePhase(Phase phase);
}
