package ru.irlix.evaluation.service.estimation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.dto.request.EstimationRequest;
import ru.irlix.evaluation.dao.dto.response.EstimationResponse;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.mapper.EstimationMapper;
import ru.irlix.evaluation.repository.EstimationRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class EstimationServiceImpl implements EstimationService {

    private final EstimationRepository estimationRepository;
    private final EstimationMapper mapper;

    @Override
    public EstimationResponse createOrUpdateEstimation(EstimationRequest estimationRequest) {
        Estimation estimation = mapper.estimationRequestToEstimation(estimationRequest);
        Estimation savedEstimation = estimationRepository.save(estimation);
        return mapper.estimationToEstimationResponse(savedEstimation);
    }

    @Override
    public EstimationResponse findEstimationById(Long id) {
        return mapper.estimationToEstimationResponse(estimationRepository.findEstimationById(id));
    }

    @Override
    public List<EstimationResponse> findAll() {
        return mapper.estimationListToEstimationsResponseList(estimationRepository.findAll());
    }

    @Override
    public void deleteEstimationById(Long id) {
        if(estimationRepository.existsById(id)){
            estimationRepository.deleteById(id);
        }
    }
}
