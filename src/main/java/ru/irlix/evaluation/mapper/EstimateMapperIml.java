package ru.irlix.evaluation.mapper;

import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Estimate;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.EstimateDTO;
import ru.irlix.evaluation.dto.PhaseDTO;

import java.time.Instant;
import java.util.List;

@Component
public class EstimateMapperIml implements EstimateMapper{
    @Override
    public Estimate estimateDtoToEstimate(EstimateDTO estimateDto) {
        if( estimateDto == null )
        {
            return null;
        }
        Estimate estimate = new Estimate();
        estimate.setId(estimateDto.getId());
        estimate.setName(estimateDto.getName());
        estimate.setCreateDate(Instant.parse(estimateDto.getCreateDate()));
        estimate.setDescription(estimateDto.getDescription());
        estimate.setRisk(estimateDto.getRisk());
        estimate.setStatus(estimateDto.getStatus());
        estimate.setClient(estimateDto.getClient());
        estimate.setCreator(estimateDto.getCreator());
        estimate.setPhases(phasesDtoToPhases(estimateDto.getPhases()));
        return estimate;
    }

    @Override
    public EstimateDTO estimateToEstimateDto(Estimate estimate) {
        if( estimate == null )
        {
            return null;
        }
        EstimateDTO estimateDTO = new EstimateDTO();
        estimateDTO.setId(estimate.getId());
        estimateDTO.setName(estimate.getName());
        estimateDTO.setCreateDate(estimate.getCreateDate().toString());
        estimateDTO.setDescription(estimate.getDescription());
        estimateDTO.setRisk(estimate.getRisk());
        estimateDTO.setStatus(estimate.getStatus());
        estimateDTO.setClient(estimate.getClient());
        estimateDTO.setCreator(estimate.getCreator());
        estimateDTO.setPhases(phasesToPhasesDto(estimate.getPhases()));
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

    @Override
    public List<PhaseDTO> phasesToPhasesDto(List<Phase> phases) {
        return null;
    }

    @Override
    public List<Phase> phasesDtoToPhases(List<PhaseDTO> phasesDTO) {
        return null;
    }
}
