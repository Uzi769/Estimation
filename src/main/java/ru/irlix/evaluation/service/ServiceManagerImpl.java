package ru.irlix.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dto.EstimateDTO;
import ru.irlix.evaluation.mapper.EstimateMapper;

@Service
@RequiredArgsConstructor
public class ServiceManagerImpl implements ServiceManager {

    private final EstimateMapper mapper;
    private final EstimateService estimateService;
    private final PhaseService phaseService;

    @Override
    public void saveEstimate(EstimateDTO estimateDTO) {
        phaseService.savePhases(mapper.phasesDtoToPhases(estimateDTO.getPhases()));
        estimateService.saveEstimate(mapper.estimateDtoToEstimate(estimateDTO));
    }
}
