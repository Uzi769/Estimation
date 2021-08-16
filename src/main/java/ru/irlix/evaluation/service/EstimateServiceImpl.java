package ru.irlix.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.repository.EstimateRepository;
import ru.irlix.evaluation.dto.EstimateDTO;
import ru.irlix.evaluation.mapper.EstimateMapper;

@Service
@RequiredArgsConstructor
public class EstimateServiceImpl implements EstimateService{

    private final EstimateRepository estimateRepository;
    private final EstimateMapper mapper;

    @Override
    public void saveEstimate(EstimateDTO estimateDTO) {
        estimateRepository.save(mapper.estimateDtoToEstimate(estimateDTO));
    }

}
