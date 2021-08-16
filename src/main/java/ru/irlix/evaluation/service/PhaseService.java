package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.PhaseDTO;

import java.util.List;

public interface PhaseService {
    void savePhases(List<PhaseDTO> phasesDTO);
    void savePhase(Phase phase);
}
