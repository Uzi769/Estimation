package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.service.PhaseService;
import ru.irlix.evaluation.utils.UrlConstants;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/phases")
@RequiredArgsConstructor
public class PhaseController {

    private final PhaseService phaseService;

    @PostMapping
    public PhaseResponse createPhase(@RequestBody @Valid PhaseRequest phaseRequest) {
        return phaseService.createPhase(phaseRequest);
    }

    @PutMapping("/{id}")
    public PhaseResponse updatePhase(@PathVariable Long id,
                                     @RequestBody PhaseRequest phaseRequest) {
        return phaseService.updatePhase(id, phaseRequest);
    }

    @GetMapping("/{id}")
    public PhaseResponse findPhaseById(@PathVariable("id") Long id) {
        return phaseService.findPhaseResponseById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePhase(@PathVariable("id") Long id) {
        phaseService.deletePhase(id);
    }

    @GetMapping("/estimation/{id}")
    public Set<PhaseResponse> findPhasesByEstimationId(@PathVariable Long id) {
        return phaseService.getPhaseSetByEstimationId(id);
    }
}
