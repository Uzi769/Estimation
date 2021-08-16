package ru.irlix.evaluation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.dto.request.EstimationRequest;
import ru.irlix.evaluation.dao.dto.response.EstimationResponse;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.mapper.EstimateMapper;
import ru.irlix.evaluation.repository.EstimateRepository;

@Service
@AllArgsConstructor
public class EstimationServiceImpl implements EstimationService {

    private EstimateRepository estimateRepository;
    private EstimateMapper mapper;

    @Override
    public EstimationResponse createEstimation(EstimationRequest estimationRequest) {
        Estimation estimation = mapper.estimationRequestToEstimation(estimationRequest);
        Estimation savedEstimation = estimateRepository.save(estimation);

        return mapper.estimationToEstimationResponse(savedEstimation);
    }
}
