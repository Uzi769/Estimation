package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.service.estimation.EstimationService;
import ru.irlix.evaluation.utils.Constants;

import java.util.List;

@RestController
@RequestMapping(Constants.BASE_URL + "/estimations")
@RequiredArgsConstructor
public class EstimationController {

    private final EstimationService estimationService;

    @PostMapping
    public ResponseEntity<EstimationResponse> createEstimation(@RequestBody EstimationRequest request) {
        EstimationResponse estimation = estimationService.createEstimation(request);
        return new ResponseEntity<>(estimation, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EstimationResponse>> findAllEstimations() {
        List<EstimationResponse> estimationList = estimationService.findAllEstimations();
        return new ResponseEntity<>(estimationList, HttpStatus.OK);
    }
}
