package ru.irlix.evaluation.service.estimation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.dto.request.EstimationRequest;
import ru.irlix.evaluation.dao.dto.response.EstimationResponse;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.mapper.EstimationMapper;
import ru.irlix.evaluation.repository.EstimationRepository;

@Service
@AllArgsConstructor
public class EstimationServiceImpl implements EstimationService {

    private EstimationRepository estimationRepository;
    private EstimationMapper mapper;

    @Override
    public EstimationResponse createEstimation(EstimationRequest estimationRequest) {
        Estimation estimation = mapper.estimationRequestToEstimation(estimationRequest);
        Estimation savedEstimation = estimationRepository.save(estimation);

        return mapper.estimationToEstimationResponse(savedEstimation);
    }
}
