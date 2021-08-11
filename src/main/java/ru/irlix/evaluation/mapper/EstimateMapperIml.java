package ru.irlix.evaluation.mapper;

import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Estimate;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.EstimateDTO;
import ru.irlix.evaluation.dto.PhaseDTO;

@Component
public class EstimateMapperIml implements EstimateMapper{
    @Override
    public Estimate estimateDtoToEstimate(EstimateDTO estimateDto) {
        if( estimateDto == null )
        {
            return null;
        }
        Estimate estimate = new Estimate();
        return estimate;
    }

    @Override
    public EstimateDTO estimateToEstimateDto(Estimate estimate) {
        if( estimate == null )
        {
            return null;
        }
        EstimateDTO estimateDTO = new EstimateDTO();
        return estimateDTO;
    }

    @Override
    public PhaseDTO phaseToPhaseDto(Phase phase) {
        if( phase == null )
        {
            return null;
        }
        PhaseDTO phaseDTO = new PhaseDTO();
        return phaseDTO;
    }

    @Override
    public Phase phaseDtoToPhase(PhaseDTO phaseDTO) {
        if( phaseDTO == null )
        {
            return null;
        }
        Phase phase = new Phase();
        return phase;
    }
}
