package ru.irlix.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.repository.PhaseRepository;
import ru.irlix.evaluation.dto.PhaseDTO;
import ru.irlix.evaluation.mapper.EstimateMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhaseServiceImpl implements PhaseService {

    private final PhaseRepository phaseRepository;
    private final EstimateMapper mapper;

    @Override
    public void savePhases(List<PhaseDTO> phasesDTO) {
        for (PhaseDTO phaseDTO: phasesDTO) {
            savePhase(mapper.phaseDtoToPhase(phaseDTO));
        }
    }

    @Override
    public void savePhase(Phase phase) {
        phaseRepository.save(phase);
    }

}
