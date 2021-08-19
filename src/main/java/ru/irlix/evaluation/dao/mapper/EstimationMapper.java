package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.*;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.service.status.StatusService;

import java.util.List;

@Mapper(componentModel = "spring", uses = PhaseMapper.class)
public abstract class EstimationMapper {

    @Mapping(target = "status", ignore = true)
    public abstract Estimation estimationRequestToEstimation(EstimationRequest estimationRequest, @Context StatusService statusService);

    public abstract EstimationResponse estimationToEstimationResponse(Estimation estimation);

    public abstract List<EstimationResponse> estimationToEstimationResponse(List<Estimation> estimationList);

    @AfterMapping
    protected void map(@MappingTarget Estimation estimation, EstimationRequest req, @Context StatusService statusService) {
        estimation.setStatus(statusService.findByName(req.getStatus()));
    }
}
