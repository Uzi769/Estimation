package ru.irlix.evaluation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.entity.Estimate;
import ru.irlix.evaluation.dao.repository.EstimateRepository;
import ru.irlix.evaluation.dto.EstimateDTO;
import ru.irlix.evaluation.mapper.EstimateMapper;

@Service
@AllArgsConstructor
public class EstimateServiceImpl implements EstimateService{

    private EstimateRepository estimateRepository;
    private EstimateMapper mapper;

    @Override
    public void saveEstimate(EstimateDTO estimateDTO) {
        Estimate estimate = mapper.estimateDtoToEstimate(estimateDTO);
        estimateRepository.save(estimate);
    }
}
