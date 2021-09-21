package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.PhaseRequest;
import ru.irlix.evaluation.dto.request.PhaseUpdateRequest;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.service.PhaseService;
import ru.irlix.evaluation.utils.constant.UrlConstants;
import ru.irlix.evaluation.utils.marker.OnCreate;
import ru.irlix.evaluation.utils.marker.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/phases")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Log4j2
public class PhaseController {

    private final PhaseService phaseService;

    @PreAuthorize("!hasRole('ROLE_ADMIN')")
    @PostMapping
    @Validated(OnCreate.class)
    public PhaseResponse createPhase(@RequestBody @Valid PhaseRequest phaseRequest) {
        log.info(UrlConstants.RECEIVED_ENTITY);
        return phaseService.createPhase(phaseRequest);
    }

    @PreAuthorize("!hasRole('ROLE_ADMIN')")
    @PostMapping("/list")
    @Validated(OnCreate.class)
    public List<PhaseResponse> createPhases(@RequestBody @Valid List<PhaseRequest> phaseRequests) {
        return phaseService.createPhases(phaseRequests);
    }

    @PreAuthorize("!hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Validated(OnUpdate.class)
    public PhaseResponse updatePhase(@PathVariable @Positive(message = "{id.positive}") Long id,
                                     @RequestBody @Valid PhaseRequest phaseRequest) {
        log.info(UrlConstants.RECEIVED_ENTITY_ID + id);
        return phaseService.updatePhase(id, phaseRequest);
    }

    @PreAuthorize("!hasRole('ROLE_ADMIN')")
    @PutMapping("/list")
    @Validated(OnUpdate.class)
    public List<PhaseResponse> updatePhases(@RequestBody @Valid List<PhaseUpdateRequest> phaseRequests) {
        log.info(UrlConstants.RECEIVED_ENTITY);
        return phaseService.updatePhases(phaseRequests);
    }

    @GetMapping("/{id}")
    public PhaseResponse findPhaseById(@PathVariable("id") @Positive(message = "{id.positive}") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        return phaseService.findPhaseResponseById(id);
    }

    @PreAuthorize("!hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deletePhase(@PathVariable("id") @Positive(message = "{id.positive}") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        phaseService.deletePhase(id);
    }
}
