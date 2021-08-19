package ru.irlix.evaluation.service.phase;

import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.response.PhaseResponse;

import java.util.List;
import java.util.Set;

public interface PhaseService {
    void createPhases(Set<Phase> phases);
}
