package ru.irlix.evaluation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.irlix.evaluation.dto.EstimateDTO;
import ru.irlix.evaluation.mapper.EstimateMapper;
import ru.irlix.evaluation.service.EstimateService;

@RestController
public class MainController {

    private EstimateService estimateService;

    @Autowired
    public void setEstimateService(EstimateService estimateService) {
        this.estimateService = estimateService;
    }

    @PostMapping("/save")
    public String saveEstimate(EstimateDTO estimateDTO){

        return "saving page";
    }

}
