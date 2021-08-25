package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.service.PhaseService;
import ru.irlix.evaluation.utils.UrlConstants;
import ru.irlix.evaluation.utils.marker.OnCreate;
import ru.irlix.evaluation.utils.marker.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/phases")
@RequiredArgsConstructor
@Validated
@CrossOrigin
public class PhaseController {

    private final PhaseService phaseService;

    @PostMapping
    @Validated(OnCreate.class)
    public PhaseResponse createPhase(@RequestBody @Valid PhaseRequest phaseRequest) {
        return phaseService.createPhase(phaseRequest);
    }

    @PutMapping("/{id}")
    @Validated(OnUpdate.class)
    public PhaseResponse updatePhase(@PathVariable @Positive(message = "{id.positive}") Long id,
                                     @RequestBody @Valid PhaseRequest phaseRequest) {
        return phaseService.updatePhase(id, phaseRequest);
    }

    @GetMapping("/{id}")
    public PhaseResponse findPhaseById(@PathVariable("id") @Positive(message = "{id.positive}") Long id) {
        return phaseService.findPhaseResponseById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePhase(@PathVariable("id") @Positive(message = "{id.positive}") Long id) {
        phaseService.deletePhase(id);
    }
}
