package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;

@Mapper(componentModel = "spring", uses = EstimationMapper.class)
public interface PhaseMapper {

    Phase phaseRequestToPhase(PhaseRequest phaseRequest);

    @Mapping(target = "estimation", ignore = true)
    PhaseResponse phaseToPhaseResponse(Phase phase);

    @AfterMapping
    default void map(@MappingTarget PhaseResponse response, Phase phase) {
        response.setEstimation(phase.getEstimation().getId());
    }
}
