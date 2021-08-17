package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dao.dto.request.EstimationRequest;
import ru.irlix.evaluation.dao.dto.response.EstimationResponse;
import ru.irlix.evaluation.service.estimation.EstimationService;
import ru.irlix.evaluation.utils.Constants;

import java.util.List;

@RestController
@RequestMapping(Constants.BASE_URL + "/estimation")
@RequiredArgsConstructor
public class EstimationController {

    private final EstimationService estimationService;

    @PostMapping
    public ResponseEntity<EstimationResponse> createEstimation(@RequestBody EstimationRequest estimationRequest) {
        EstimationResponse estimationResponse = estimationService.createOrUpdateEstimation(estimationRequest);
        return new ResponseEntity<>(estimationResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstimationResponse> getEstimationById(@PathVariable("id") Long id){
        EstimationResponse estimationResponse = estimationService.findEstimationById(id);
        return new ResponseEntity<>(estimationResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EstimationResponse>> getAllEstimations(){
        List<EstimationResponse> estimationResponseList = estimationService.findAll();
        return new ResponseEntity<>(estimationResponseList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteEstimationById(@PathVariable("id") Long id){
        estimationService.deleteEstimationById(id);
    }
    @PutMapping
    public ResponseEntity<EstimationResponse> updateEstimation(@RequestBody EstimationRequest estimationRequest){
        EstimationResponse estimationResponse = estimationService.createOrUpdateEstimation(estimationRequest);
        return new ResponseEntity<>(estimationResponse, HttpStatus.OK);
    }
}
