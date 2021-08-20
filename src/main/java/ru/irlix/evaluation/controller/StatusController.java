package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.StatusRequest;
import ru.irlix.evaluation.dto.response.StatusResponse;
import ru.irlix.evaluation.service.StatusService;
import ru.irlix.evaluation.utils.UrlConstants;

import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "statuses")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @PostMapping
    public StatusResponse createStatus(@RequestBody StatusRequest statusRequest) {
        return statusService.createStatus(statusRequest);
    }

    @PutMapping("/{id}")
    public StatusResponse updateStatus(@PathVariable("id") Long id,
                                       @RequestBody StatusRequest statusRequest) {
        return statusService.updateStatus(id, statusRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable("id") Long id) {
        statusService.deleteStatus(id);
    }

    @GetMapping("/{id}")
    public StatusResponse findStatusById(@PathVariable("id") Long id) {
        return statusService.findStatusResponseById(id);
    }

    @GetMapping
    public List<StatusResponse> findAllStatuses() {
        return statusService.findAllStatuses();
    }
}
