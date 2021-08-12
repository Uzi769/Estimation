package ru.irlix.evaluation.mapper;

import org.mapstruct.Mapper;
import ru.irlix.evaluation.dao.entity.Estimate;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.EstimateDTO;
import ru.irlix.evaluation.dto.PhaseDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EstimateMapper {

    Estimate estimateDtoToEstimate(EstimateDTO estimateDto);
    EstimateDTO estimateToEstimateDto(Estimate estimate);

    PhaseDTO phaseToPhaseDto(Phase phase);
    Phase phaseDtoToPhase(PhaseDTO phaseDTO);

    List<PhaseDTO> phasesToPhasesDto (List<Phase> phases);
    List<Phase> phasesDtoToPhases (List<PhaseDTO> phasesDTO);

}
