package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.service.estimation.EstimationService;
import ru.irlix.evaluation.utils.UrlConstants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/estimations")
@RequiredArgsConstructor
public class EstimationController {

    private final EstimationService estimationService;

    @PostMapping
    public EstimationResponse createEstimation(@RequestBody @Valid EstimationRequest request) {
        return estimationService.createEstimation(request);
    }

    @PutMapping("/{id}")
    public EstimationResponse deleteEstimation(@PathVariable Long id, @RequestBody EstimationRequest request) {
        return estimationService.updateEstimation(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteEstimation(@PathVariable Long id) {
        estimationService.deleteEstimation(id);
    }

    @GetMapping
    public List<EstimationResponse> findAllEstimations(EstimationFilterRequest request) {
        return estimationService.findAllEstimations(request);
    }

    @GetMapping("/{id}")
    public EstimationResponse findEstimationById(@PathVariable Long id) {
        return estimationService.findEstimationResponseById(id);
    }
}
