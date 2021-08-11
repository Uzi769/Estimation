package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dto.EstimateDTO;

public interface EstimateService {
    public boolean saveEstimate(EstimateDTO estimateDTO);
}
