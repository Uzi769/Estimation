package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.*;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.repository.estimation.EstimationRepository;

import java.util.Set;

@Mapper(componentModel = "spring", uses = EstimationMapper.class)
public abstract class PhaseMapper {

    @Mapping(target = "estimation", ignore = true)
    public abstract Phase phaseRequestToPhase(PhaseRequest phaseRequest,
                                              @Context EstimationRepository estimationRepository);

    @Mapping(target = "estimationId", ignore = true)
    public abstract PhaseResponse phaseToPhaseResponse(Phase phase);

    @Mapping(target = "estimationId", ignore = true)
    public abstract Set<PhaseResponse> phaseToPhaseResponse(Set<Phase> phase);

    @AfterMapping
    protected void map(@MappingTarget Phase phase,
                       PhaseRequest request,
                       @Context EstimationRepository estimationRepository) {
        Estimation estimation = estimationRepository.findById(request.getEstimationId())
                .orElseThrow(() -> new NotFoundException("Estimation with id " + request.getEstimationId() + " not found"));
        phase.setEstimation(estimation);
    }

    @AfterMapping
    protected void map(@MappingTarget PhaseResponse response, Phase phase) {
        response.setEstimationId(phase.getEstimation().getId());
    }

}
