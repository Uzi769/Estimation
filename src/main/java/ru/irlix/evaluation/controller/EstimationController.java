package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.service.estimation.EstimationService;
import ru.irlix.evaluation.utils.UrlConstants;

import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/estimations")
@RequiredArgsConstructor
public class EstimationController {

    private final EstimationService estimationService;

    @PostMapping
    public EstimationResponse createEstimation(@RequestBody EstimationRequest request) {
        return estimationService.createEstimation(request);
    }

    @GetMapping
    public List<EstimationResponse> findAllEstimations() {
        return estimationService.findAllEstimations();
    }
}
