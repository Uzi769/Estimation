package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.TaskRequest;
import ru.irlix.evaluation.dto.request.TaskUpdateRequest;
import ru.irlix.evaluation.dto.response.TaskResponse;
import ru.irlix.evaluation.service.TaskService;
import ru.irlix.evaluation.utils.constant.UrlConstants;
import ru.irlix.evaluation.utils.marker.OnCreate;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/tasks")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Log4j2
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("!hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
    @PostMapping
    @Validated(OnCreate.class)
    public TaskResponse createTask(@RequestBody @Valid TaskRequest request) {
        log.info(UrlConstants.RECEIVED_ENTITY);
        return taskService.createTask(request);
    }

    @PreAuthorize("!hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
    @PostMapping("/list")
    @Validated(OnCreate.class)
    public List<TaskResponse> createTasks(@RequestBody @Valid List<TaskRequest> requests) {
        return taskService.createTasks(requests);
    }

    @PreAuthorize("!hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable @Positive(message = "{id.positive}") Long id,
                                   @RequestBody @Valid TaskRequest request) {
        log.info(UrlConstants.RECEIVED_ENTITY_ID + id);
        return taskService.updateTask(id, request);
    }

    @PreAuthorize("!hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
    @PutMapping("/list")
    public List<TaskResponse> updateTask(@RequestBody @Valid List<TaskUpdateRequest> request) {
        log.info(UrlConstants.RECEIVED_ENTITY);
        return taskService.updateTasks(request);
    }

    @GetMapping("/{id}")
    public TaskResponse findTaskById(@PathVariable @Positive(message = "{id.positive}") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        return taskService.findTaskResponseById(id);
    }

    @PreAuthorize("!hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable @Positive(message = "{id.positive}") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        taskService.deleteTask(id);
    }

    @GetMapping
    public List<TaskResponse> findTasks(@RequestParam("phaseId") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        return taskService.findTasks(id);
    }
}
