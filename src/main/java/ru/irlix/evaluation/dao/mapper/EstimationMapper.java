package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.*;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.repository.StatusRepository;
import ru.irlix.evaluation.utils.EntityConstants;

import java.util.List;

@Mapper(componentModel = "spring", uses = PhaseMapper.class)
public abstract class EstimationMapper {

    @Mapping(target = "status", ignore = true)
    public abstract Estimation estimationRequestToEstimation(EstimationRequest estimationRequest,
                                                             @Context StatusRepository statusRepository);

    @Mapping(target = "status", ignore = true)
    public abstract EstimationResponse estimationToEstimationResponse(Estimation estimation);

    public abstract List<EstimationResponse> estimationToEstimationResponse(List<Estimation> estimationList);

    @AfterMapping
    @Transactional
    protected void map(@MappingTarget Estimation estimation,
                       EstimationRequest req,
                       @Context StatusRepository statusRepository) {
        if (req.getStatus() == null) {
            req.setStatus(EntityConstants.DEFAULT_STATUS_ID);
        }

        Status status = statusRepository.findById(req.getStatus())
                .orElseThrow(() -> new NotFoundException("Status with id " + req.getStatus() + " not found"));
        estimation.setStatus(status);
    }

    @AfterMapping
    protected void map(@MappingTarget EstimationResponse response, Estimation estimation) {
        if (estimation.getStatus() != null) {
            response.setStatus(estimation.getStatus().getId());
        }
    }
}
