package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.EstimationFilterRequest;
import ru.irlix.evaluation.dto.request.EstimationFindAnyRequest;
import ru.irlix.evaluation.dto.request.EstimationRequest;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.service.EstimationService;
import ru.irlix.evaluation.utils.constant.UrlConstants;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/estimations")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Log4j2
public class EstimationController {

    private final EstimationService estimationService;

    @PreAuthorize("hasRole('ROLE_SALES')")
    @PostMapping
    public EstimationResponse createEstimation(@RequestBody EstimationRequest request) {
        log.info(UrlConstants.RECEIVED_ENTITY);
        return estimationService.createEstimation(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
    @PutMapping("/{id}")
    public EstimationResponse updateEstimation(@PathVariable @Positive(message = "{id.positive}") Long id,
                                               @RequestBody EstimationRequest request) {
        log.info(UrlConstants.RECEIVED_ENTITY_ID + id);
        return estimationService.updateEstimation(id, request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteEstimation(@PathVariable @Positive(message = "{id.positive}") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        estimationService.deleteEstimation(id);
    }

    @GetMapping
    public Page<EstimationResponse> findAllEstimations(@Valid EstimationFilterRequest request) {
        log.info(UrlConstants.RECEIVED_FILTER + request);
        return estimationService.findAllEstimations(request);
    }

    @GetMapping("/match")
    public Page<EstimationResponse> findAllEstimations(@Valid EstimationFindAnyRequest request) {
        log.info(UrlConstants.RECEIVED_FILTER + request);
        return estimationService.findAnyEstimations(request);
    }

    @GetMapping("/{id}")
    public EstimationResponse findEstimationById(@PathVariable @Positive(message = "{id.positive}") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        return estimationService.findEstimationResponseById(id);
    }

    @GetMapping("/{id}/phases")
    public List<PhaseResponse> findPhasesByEstimationId(@PathVariable Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        return estimationService.findPhaseResponsesByEstimationId(id);
    }

    @PreAuthorize("hasRole('ROLE_SALES')")
    @GetMapping("/{id}/report")
    public ResponseEntity<Resource> getEstimationsReport(@PathVariable Long id, ReportRequest request) throws IOException {
        Resource resource = estimationService.getEstimationsReport(id, request);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
    }
}
