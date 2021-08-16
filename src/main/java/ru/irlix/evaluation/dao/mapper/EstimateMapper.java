package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.Mapper;
import ru.irlix.evaluation.dao.dto.request.EstimationRequest;
import ru.irlix.evaluation.dao.dto.response.EstimationResponse;
import ru.irlix.evaluation.dao.entity.Estimation;

@Mapper(componentModel = "spring")
public interface EstimateMapper {

    Estimation estimationRequestToEstimation(EstimationRequest estimationRequest);

    EstimationResponse estimationToEstimationResponse(Estimation estimation);
}
