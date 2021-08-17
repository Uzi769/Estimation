package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.Mapper;
import ru.irlix.evaluation.dao.dto.request.EstimationRequest;
import ru.irlix.evaluation.dao.dto.response.EstimationResponse;
import ru.irlix.evaluation.dao.entity.Estimation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EstimationMapper {

    Estimation estimationRequestToEstimation(EstimationRequest estimationRequest);
    EstimationResponse estimationToEstimationResponse(Estimation estimation);

    List<Estimation> estimationRequestListToEstimationList(List<EstimationRequest> estimationRequestList);
    List<EstimationResponse> estimationListToEstimationsResponseList(List<Estimation> estimationList);
}
