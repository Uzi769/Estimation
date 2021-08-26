package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.TaskRequest;
import ru.irlix.evaluation.dto.response.TaskResponse;
import ru.irlix.evaluation.service.TaskService;
import ru.irlix.evaluation.utils.UrlConstants;
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

    @PostMapping
    @Validated(OnCreate.class)
    public TaskResponse createTask(@RequestBody @Valid TaskRequest request) {
        log.info(UrlConstants.RECEIVED_ENTITY);
        return taskService.createTask(request);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable @Positive(message = "{id.positive}") Long id,
                                   @RequestBody @Valid TaskRequest request) {
        log.info(UrlConstants.RECEIVED_ENTITY_ID + id);
        return taskService.updateTask(id, request);
    }

    @GetMapping("/{id}")
    public TaskResponse findTaskById(@PathVariable @Positive(message = "{id.positive}") Long id) {
        log.info(UrlConstants.RECEIVED_ID + id);
        return taskService.findTaskResponseById(id);
    }

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
