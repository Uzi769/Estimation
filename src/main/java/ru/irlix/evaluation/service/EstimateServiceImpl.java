package ru.irlix.evaluation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.entity.Estimate;
import ru.irlix.evaluation.dao.repository.EstimateRepository;

@Service
@RequiredArgsConstructor
public class EstimateServiceImpl implements EstimateService{

    private final EstimateRepository estimateRepository;

    @Override
    public void saveEstimate(Estimate estimate) {

        estimateRepository.save(estimate);

    }

}
