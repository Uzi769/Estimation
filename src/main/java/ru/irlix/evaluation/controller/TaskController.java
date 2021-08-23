package ru.irlix.evaluation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.irlix.evaluation.dto.request.TaskRequest;
import ru.irlix.evaluation.dto.response.TaskResponse;
import ru.irlix.evaluation.service.TaskService;
import ru.irlix.evaluation.utils.UrlConstants;
import ru.irlix.evaluation.utils.marker.OnCreate;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Set;

@RestController
@RequestMapping(UrlConstants.BASE_URL + "/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Validated(OnCreate.class)
    public TaskResponse createTask(@RequestBody @Valid TaskRequest request) {
        return taskService.createTask(request);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable @Positive Long id,
                                   @RequestBody @Valid TaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @GetMapping("/{id}")
    public TaskResponse findTaskById(@PathVariable @Positive Long id) {
        return taskService.findTaskResponseById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable @Positive Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping
    public Set<TaskResponse> findTasks(@RequestParam("phaseId") @Positive Long id) {
        return taskService.findTasks(id);
    }
}
