package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.EstimateDTO;

import java.util.List;

public interface EstimateService {
    void saveEstimate(EstimateDTO estimateDTO);
    void savePhases(List<Phase> phases);
    void savePhase(Phase phase);
}
