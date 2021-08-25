package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.service.EstimationService;
import ru.irlix.evaluation.utils.UrlConstants;
import ru.irlix.evaluation.utils.ValidationMessage;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/estimations")
@RequiredArgsConstructor
@Validated
@CrossOrigin
public class EstimationController {

    private final EstimationService estimationService;

    @PostMapping
    public EstimationResponse createEstimation(@RequestBody EstimationRequest request) {
        return estimationService.createEstimation(request);
    }

    @PutMapping("/{id}")
    public EstimationResponse updateEstimation(@PathVariable @Positive(message = ValidationMessage.ID) Long id,
                                               @RequestBody EstimationRequest request) {
        return estimationService.updateEstimation(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteEstimation(@PathVariable @Positive(message = ValidationMessage.ID) Long id) {
        estimationService.deleteEstimation(id);
    }

    @GetMapping
    public List<EstimationResponse> findAllEstimations(EstimationFilterRequest request) {
        return estimationService.findAllEstimations(request);
    }

    @GetMapping("/{id}")
    public EstimationResponse findEstimationById(@PathVariable @Positive(message = ValidationMessage.ID) Long id) {
        return estimationService.findEstimationResponseById(id);
    }

    @GetMapping("/{id}/phases")
    public List<PhaseResponse> findPhasesByEstimationId(@PathVariable Long id) {
        return estimationService.findPhaseResponsesByEstimationId(id);
    }
}
