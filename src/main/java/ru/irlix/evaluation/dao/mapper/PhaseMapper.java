package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.*;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.service.estimation.EstimationService;

import java.util.Set;

@Mapper(componentModel = "spring", uses = EstimationMapper.class)
public abstract class PhaseMapper {

    @Mapping(target = "estimation", ignore = true)
    public abstract Phase phaseRequestToPhase(PhaseRequest phaseRequest, @Context EstimationService estimationService);

    @Mapping(target = "estimationId", ignore = true)
    public abstract PhaseResponse phaseToPhaseResponse(Phase phase);

    @Mapping(target = "estimationId", ignore = true)
    public abstract Set<PhaseResponse> phaseToPhaseResponse(Set<Phase> phase);

    @AfterMapping
    @Transactional
    protected void map(@MappingTarget Phase phase, PhaseRequest request, @Context EstimationService estimationService) {
        Estimation estimation = estimationService.findEstimationById(request.getEstimationId());
        phase.setEstimation(estimation);
    }

    @AfterMapping
    @Transactional
    protected void map(@MappingTarget PhaseResponse response, Phase phase) {
        response.setEstimationId(phase.getEstimation().getId());
    }

}
