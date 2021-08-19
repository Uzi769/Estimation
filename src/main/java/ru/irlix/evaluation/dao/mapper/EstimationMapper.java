package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.*;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.service.phase.PhaseService;
import ru.irlix.evaluation.service.status.StatusService;
import ru.irlix.evaluation.utils.EntityConstants;

import java.util.List;

@Mapper(componentModel = "spring", uses = PhaseMapper.class)
public abstract class EstimationMapper {

    @Mapping(target = "status", ignore = true)
    public abstract Estimation estimationRequestToEstimation(EstimationRequest estimationRequest, @Context StatusService statusService);

    @Mapping(target = "status", ignore = true)
    public abstract EstimationResponse estimationToEstimationResponse(Estimation estimation);

    public abstract List<EstimationResponse> estimationToEstimationResponse(List<Estimation> estimationList);

    @AfterMapping
    @Transactional
    protected void map(@MappingTarget Estimation estimation,
                       EstimationRequest req,
                       @Context StatusService statusService) {
        if (req.getStatus() == null) {
            req.setStatus(EntityConstants.DEFAULT_STATUS);
        }

        Status status = statusService.findByValue(req.getStatus());
        estimation.setStatus(status);
    }

    @AfterMapping
    protected void map(@MappingTarget EstimationResponse response, Estimation estimation) {
        if (estimation.getStatus() != null) {
            response.setStatus(estimation.getStatus().getDisplayValue());
        }
    }
}
