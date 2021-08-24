package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.dao.mapper.helper.StatusHelper;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = PhaseMapper.class)
public abstract class EstimationMapper {

    @Autowired
    protected StatusHelper statusHelper;

    @Mapping(target = "status", ignore = true)
    public abstract Estimation estimationRequestToEstimation(EstimationRequest estimationRequest);

    @Mapping(target = "status", ignore = true)
    public abstract EstimationResponse estimationToEstimationResponse(Estimation estimation);

    public abstract List<EstimationResponse> estimationToEstimationResponse(List<Estimation> estimationList);

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
    }

    @AfterMapping
    protected void map(@MappingTarget List<EstimationResponse> estimationResponses) {
        List<EstimationResponse> sortedResponse = estimationResponses.stream()
                .sorted(Comparator.comparing(EstimationResponse::getId))
                .collect(Collectors.toList());

        for (int i = 0; i < estimationResponses.size(); i++) {
            estimationResponses.set(i, sortedResponse.get(i));
        }
    }
}
