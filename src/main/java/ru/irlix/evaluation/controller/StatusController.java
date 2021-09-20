package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.StatusRequest;
import ru.irlix.evaluation.dto.response.StatusResponse;
import ru.irlix.evaluation.service.StatusService;
import ru.irlix.evaluation.utils.constant.UrlConstants;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/statuses")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Log4j2
public class StatusController {

    private final StatusService statusService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public StatusResponse createStatus(@RequestBody @Positive(message = "{id.positive}") StatusRequest statusRequest) {
        log.info(UrlConstants.RECEIVED_ENTITY);
        return statusService.createStatus(statusRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public StatusResponse updateStatus(@PathVariable("id") @Positive(message = "{id.positive}") Long id,
                                       @RequestBody StatusRequest statusRequest) {
        log.info(UrlConstants.RECEIVED_ENTITY_ID + id);
        return statusService.updateStatus(id, statusRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable("id") @Positive(message = "{id.positive}") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        statusService.deleteStatus(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SALES')")
    @GetMapping("/{id}")
    public StatusResponse findStatusById(@PathVariable("id") @Positive(message = "{id.positive}") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        return statusService.findStatusResponseById(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SALES')")
    @GetMapping
    public List<StatusResponse> findAllStatuses() {
        log.info(UrlConstants.RECEIVED_NO_ARGS);
        return statusService.findAllStatuses();
    }
}
