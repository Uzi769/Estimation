package ru.irlix.evaluation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.entity.Estimate;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.repository.EstimateRepository;
import ru.irlix.evaluation.dao.repository.PhaseRepository;
import ru.irlix.evaluation.dto.EstimateDTO;
import ru.irlix.evaluation.dto.PhaseDTO;
import ru.irlix.evaluation.mapper.EstimateMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class EstimateServiceImpl implements EstimateService {

    private EstimateRepository estimateRepository;
    private PhaseRepository phaseRepository;
    private EstimateMapper mapper;

    @Override
    public void saveEstimate(EstimateDTO estimateDTO) {

        Estimate estimate = EstimateMapper.ESTIMATE_MAPPER.estimateDtoToEstimate(estimateDTO);
        List<PhaseDTO> phasesDTO = estimateDTO.getPhases();
        for (PhaseDTO phaseDTO : phasesDTO) {
        }
        List<Phase> phases = EstimateMapper.ESTIMATE_MAPPER.phasesDtoToPhases(phasesDTO);
        savePhases(phases);
        estimateRepository.save(estimate);
    }

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
