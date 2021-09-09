package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.dao.mapper.helper.StatusHelper;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.utils.math.EstimationMath;

import java.util.List;

@Mapper(componentModel = "spring", uses = PhaseMapper.class)
public abstract class EstimationMapper {

    @Autowired
    protected StatusHelper statusHelper;

    @Mapping(target = "status", ignore = true)
    public abstract Estimation estimationRequestToEstimation(EstimationRequest estimationRequest);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "hoursMin", ignore = true)
    @Mapping(target = "hoursMax", ignore = true)
    public abstract EstimationResponse estimationToEstimationResponse(Estimation estimation);

    public abstract List<EstimationResponse> estimationToEstimationResponse(List<Estimation> estimation);

    public Page<EstimationResponse> estimationToEstimationResponse(Page<Estimation> estimations) {
        List<EstimationResponse> responses = estimationToEstimationResponse(estimations.getContent());
        return new PageImpl<>(responses, estimations.getPageable(), estimations.getTotalElements());
    }

    @AfterMapping
    protected void map(@MappingTarget Estimation estimation, EstimationRequest req) {
        Status status = statusHelper.findStatusById(req.getStatus());
        estimation.setStatus(status);
    }

    @AfterMapping
    protected void map(@MappingTarget EstimationResponse response, Estimation estimation) {
        if (estimation.getStatus() != null) {
            response.setStatus(estimation.getStatus().getId());
        }

        response.setHoursMin(EstimationMath.calcEstimationMinHours(estimation));
        response.setHoursMax(EstimationMath.calcEstimationMaxHours(estimation));
    }
}
