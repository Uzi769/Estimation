package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.StatusRequest;
import ru.irlix.evaluation.dto.response.StatusResponse;
import ru.irlix.evaluation.service.StatusService;
import ru.irlix.evaluation.utils.UrlConstants;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/statuses")
@RequiredArgsConstructor
@CrossOrigin
public class StatusController {

    private final StatusService statusService;

    @PostMapping
    public StatusResponse createStatus(@RequestBody @Positive(message = "{id.positive}") StatusRequest statusRequest) {
        return statusService.createStatus(statusRequest);
    }

    @PutMapping("/{id}")
    public StatusResponse updateStatus(@PathVariable("id") @Positive(message = "{id.positive}") Long id,
                                       @RequestBody StatusRequest statusRequest) {
        return statusService.updateStatus(id, statusRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable("id") @Positive(message = "{id.positive}") Long id) {
        statusService.deleteStatus(id);
    }

    @GetMapping("/{id}")
    public StatusResponse findStatusById(@PathVariable("id") @Positive(message = "{id.positive}") Long id) {
        return statusService.findStatusResponseById(id);
    }

    @GetMapping
    public List<StatusResponse> findAllStatuses() {
        return statusService.findAllStatuses();
    }
}
