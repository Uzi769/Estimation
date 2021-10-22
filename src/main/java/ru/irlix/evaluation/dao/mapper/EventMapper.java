package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.irlix.evaluation.dao.entity.*;
import ru.irlix.evaluation.dao.helper.EstimationHelper;
import ru.irlix.evaluation.dao.helper.PhaseHelper;
import ru.irlix.evaluation.dao.helper.UserHelper;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.dto.response.EventResponse;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.dto.response.TaskResponse;
import ru.irlix.evaluation.utils.security.SecurityUtils;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring", uses = ActionMapper.class)
public abstract class EventMapper {

    @Autowired
    private EstimationHelper estimationHelper;

    @Autowired
    private PhaseHelper phaseHelper;

    @Autowired
    private UserHelper userHelper;

    public abstract EventResponse eventToEventResponse(Event event);

    public abstract List<EventResponse> eventToEventResponse(List<Event> event);

    public Page<EventResponse> eventToEventResponse(Page<Event> events) {
        List<EventResponse> responses = eventToEventResponse(events.getContent());
        return new PageImpl<>(responses, events.getPageable(), events.getTotalElements());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estimationId", source = "id")
    @Mapping(target = "estimationName", source = "name")
    public abstract Event estimationResponseToEvent(EstimationResponse estimation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phaseId", source = "id")
    @Mapping(target = "phaseName", source = "name")
    public abstract Event phaseResponseToEvent(PhaseResponse phase);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taskId", source = "id")
    @Mapping(target = "taskName", source = "name")
    public abstract Event taskResponseToEvent(TaskResponse task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estimationId", source = "id")
    @Mapping(target = "estimationName", source = "name")
    public abstract Event estimationToEvent(Estimation estimation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phaseId", source = "id")
    @Mapping(target = "phaseName", source = "name")
    public abstract Event phaseToEvent(Phase phase);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taskId", source = "id")
    @Mapping(target = "taskName", source = "name")
    public abstract Event taskToEvent(Task task);

    @AfterMapping
    protected void map(@MappingTarget Event event) {
        String keycloakId = SecurityUtils.getKeycloakId();
        User user = userHelper.findUserByKeycloakId(keycloakId);

        event.setUserId(user.getUserId());
        event.setUsername(SecurityUtils.getUserName());
        event.setDate(Instant.now());
    }

    @AfterMapping
    protected void map(@MappingTarget Event event, PhaseResponse phase) {
        Estimation estimation = estimationHelper.findEstimationById(phase.getEstimationId());
        event.setEstimationName(estimation.getName());
    }

    @AfterMapping
    protected void map(@MappingTarget Event event, TaskResponse task) {
        Phase phase = phaseHelper.findPhaseById(task.getPhaseId());
        event.setPhaseName(phase.getName());

        Estimation estimation = phase.getEstimation();
        event.setEstimationId(estimation.getId());
        event.setEstimationName(estimation.getName());
    }

    @AfterMapping
    protected void map(@MappingTarget Event event, Phase phase) {
        Estimation estimation = phase.getEstimation();
        event.setEstimationId(estimation.getId());
        event.setEstimationName(estimation.getName());
    }

    @AfterMapping
    protected void map(@MappingTarget Event event, Task task) {
        Phase phase = task.getPhase();
        event.setPhaseId(phase.getId());
        event.setPhaseName(phase.getName());

        Estimation estimation = phase.getEstimation();
        event.setEstimationId(estimation.getId());
        event.setEstimationName(estimation.getName());
    }
}
