package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dao.entity.Phase;

import java.util.List;

public interface PhaseService {
    void savePhases(List<Phase> phases);
    void savePhase(Phase phase);
}
