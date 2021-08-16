package ru.irlix.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.repository.PhaseRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhaseServiceImpl implements PhaseService {

    private final PhaseRepository phaseRepository;

    @Override
    public void savePhases(List<Phase> phases) {
        for (Phase phase : phases) {
            savePhase(phase);
        }
    }

    @Override
    public void savePhase(Phase phase) {
        phaseRepository.save(phase);
    }

}
