package ru.irlix.evaluation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.entity.Estimate;
import ru.irlix.evaluation.dao.repository.EstimateRepository;
import ru.irlix.evaluation.dto.EstimateDTO;
import ru.irlix.evaluation.mapper.EstimateMapper;

@Service
public class EstimateServiceImpl implements EstimateService{

    private EstimateRepository estimateRepository;
    private EstimateMapper mapper;

    @Autowired
    public void setEstimateRepository(EstimateRepository estimateRepository) {
        this.estimateRepository = estimateRepository;
    }

    @Autowired
    public void setMapper(EstimateMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public boolean saveEstimate(EstimateDTO estimateDTO) {

//        Estimate estimate = new Estimate();
//        if(estimateRepository.existsEstimateById(estimate.getId()))
//        {
//            return false;
//        }
//        else
//            estimateRepository.save(estimate);
        return true;
    }
}
