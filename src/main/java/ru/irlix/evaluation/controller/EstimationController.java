package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dao.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dao.dto.request.EstimationRequest;
import ru.irlix.evaluation.dao.dto.response.EstimationResponse;
import ru.irlix.evaluation.service.estimation.EstimationService;
import ru.irlix.evaluation.utils.Constants;

import java.util.List;

@RestController
@RequestMapping(Constants.BASE_URL + "/estimations")
@RequiredArgsConstructor
public class EstimationController {

    private final EstimationService estimationService;

    @PostMapping
    public ResponseEntity<Long> createEstimation(@RequestBody EstimationRequest request) {
        Long id = estimationService.createEstimation(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstimationResponse> updateEstimation(@PathVariable Long id,
                                                               @RequestBody EstimationRequest request) {
        EstimationResponse updatedEstimation = estimationService.updateEstimation(id, request);
        return new ResponseEntity<>(updatedEstimation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EstimationResponse> updateEstimation(@PathVariable Long id) {
        estimationService.deleteEstimation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EstimationResponse>> findAllEstimations(EstimationFilterRequest request) {
        List<EstimationResponse> estimationsList = estimationService.findAllEstimations(request);
        return new ResponseEntity<>(estimationsList, HttpStatus.OK);
    }
}
