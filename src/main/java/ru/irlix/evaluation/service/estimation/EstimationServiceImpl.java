package ru.irlix.evaluation.service.estimation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.mapper.EstimationMapper;
import ru.irlix.evaluation.repository.EstimationRepository;
import ru.irlix.evaluation.service.service.StatusService;

import java.util.List;

@Service
@AllArgsConstructor
public class EstimationServiceImpl implements EstimationService {

    private EstimationRepository estimationRepository;
    private StatusService statusService;
    private EstimationMapper mapper;

    @Override
    public EstimationResponse createEstimation(EstimationRequest estimationRequest) {
        Estimation estimation = mapper.estimationRequestToEstimation(estimationRequest, statusService);
        Estimation savedEstimation = estimationRepository.save(estimation);

        return mapper.estimationToEstimationResponse(savedEstimation);
    }

    @Override
    public List<EstimationResponse> findAllEstimations() {
        List<Estimation> estimationList = estimationRepository.findAll();
        return mapper.estimationsToEstimationResponseList(estimationList);
    }
}
