package ru.irlix.evaluation.service;

import ru.irlix.evaluation.dto.EstimateDTO;

public interface EstimateService {
    boolean saveEstimate(EstimateDTO estimateDTO);
}
