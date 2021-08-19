package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;

import java.util.Set;

@Mapper(componentModel = "spring", uses = EstimationMapper.class)
public abstract class PhaseMapper {

    public abstract Phase phaseRequestToPhase(PhaseRequest phaseRequest);

    public abstract Set<Phase> phaseRequestToPhase(Set<PhaseRequest> phaseRequest);

    @Mapping(target = "estimation", ignore = true)
    public abstract PhaseResponse phaseToPhaseResponse(Phase phase);

    @AfterMapping
    protected void map(@MappingTarget PhaseResponse response, Phase phase) {
        response.setEstimation(phase.getEstimation().getId());
    }
}
