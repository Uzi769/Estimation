package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.*;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.service.status.StatusService;

import java.util.List;

@Mapper(componentModel = "spring", uses = PhaseMapper.class)
public interface EstimationMapper {
    @Mapping(target = "status", ignore = true)
    Estimation estimationRequestToEstimation(EstimationRequest estimationRequest, @Context StatusService statusService);

    EstimationResponse estimationToEstimationResponse(Estimation estimation);

    List<EstimationResponse> estimationsToEstimationResponseList(List<Estimation> estimationList);

    @AfterMapping
    default void map(@MappingTarget Estimation estimation, EstimationRequest req, @Context StatusService statusService) {
        estimation.setStatus(statusService.findByName(req.getStatus()));
    }
}
