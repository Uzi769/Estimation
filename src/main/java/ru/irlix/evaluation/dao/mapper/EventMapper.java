package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Event;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.helper.EstimationHelper;
import ru.irlix.evaluation.dao.helper.PhaseHelper;
import ru.irlix.evaluation.dto.response.EstimationResponse;
import ru.irlix.evaluation.dto.response.EventResponse;
import ru.irlix.evaluation.dto.response.PhaseResponse;
import ru.irlix.evaluation.dto.response.TaskResponse;
import ru.irlix.evaluation.utils.security.SecurityUtils;

import java.time.Instant;

@Mapper(componentModel = "spring")
public abstract class EventMapper {

    @Autowired
    private EstimationHelper estimationHelper;

    @Autowired
    private PhaseHelper phaseHelper;

    public abstract EventResponse EventToEventResponse(Event event);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userName", source = "creator")
    @Mapping(target = "estimationName", source = "name")
    public abstract Event EstimationResponseToEvent(EstimationResponse estimation);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phaseName", source = "name")
    public abstract Event phaseResponseToEvent(PhaseResponse phase);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taskName", source = "name")
    public abstract Event TaskResponseToEvent(TaskResponse task);

    @AfterMapping
    protected void map(@MappingTarget Event event) {
        event.setUserName(SecurityUtils.getUserName());
        event.setDate(Instant.now());
    }

    @AfterMapping
    protected void map(@MappingTarget Event event, PhaseResponse phase) {
        Estimation estimation = estimationHelper.findEstimationById(phase.getEstimationId());
        String estimationName = estimation.getName();
        event.setEstimationName(estimationName);
    }

    @AfterMapping
    protected void map(@MappingTarget Event event, TaskResponse task) {
        Phase phase = phaseHelper.findPhaseById(task.getPhaseId());
        String phaseName = phase.getName();
        event.setPhaseName(phaseName);

        Estimation estimation = phase.getEstimation();
        String estimationName = estimation.getName();
        event.setEstimationName(estimationName);
    }
}