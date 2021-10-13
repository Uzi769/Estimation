package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Event;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.response.EventResponse;
import ru.irlix.evaluation.service.EventService;

@Mapper(componentModel = "spring")
public abstract class EventMapper {

    @Lazy
    @Autowired
    protected EventService eventService;

    @Mappings({
            @Mapping(target = "userName", expression = "java(event.getUser().getFirstName() + \" \" + event.getUser().getLastName())"),
            @Mapping(target = "description", expression = "java(event.getValue())"),
            @Mapping(target = "target", expression = "java(event.getEstimationName())")

    })
    public abstract EventResponse EventToEventResponse(Event event);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "estimationName", ignore = true)
    @Mapping(target = "phaseName", ignore = true)
    @Mapping(target = "taskName", ignore = true)
    public abstract Event EstimationToEvent(Estimation estimation);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "estimationName", ignore = true)
    @Mapping(target = "phaseName", ignore = true)
    @Mapping(target = "taskName", ignore = true)
    public abstract Event PhaseToEvent(Phase phase);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "estimationName", ignore = true)
    @Mapping(target = "phaseName", ignore = true)
    @Mapping(target = "taskName", ignore = true)
    public abstract Event TaskToEvent(Task task);

    @AfterMapping
    protected void map(@MappingTarget Event event, Task task) {

        event.setUserName(task.getPhase().getEstimation().getUsers()
                        .stream()
                        .findFirst()
                        .map(a -> a.getFirstName() + a.getLastName())
                        .orElse(null));
        event.setEstimationName(task.getPhase().getEstimation().getName());
        event.setDate(eventService.getEventCreationDate());
        event.setEstimationName(task.getPhase().getEstimation().getName());
        event.setPhaseName(task.getPhase().getName());
        event.setTaskName(task.getName());
    }

    @AfterMapping
    protected void map(@MappingTarget Event event, Phase phase) {

        event.setUserName(phase.getEstimation().getUsers()
                .stream()
                .findFirst()
                .map(a -> a.getFirstName() + a.getLastName())
                .orElse(null));
        event.setEstimationName(phase.getEstimation().getName());
        event.setDate(eventService.getEventCreationDate());
        event.setEstimationName(phase.getEstimation().getName());
        event.setPhaseName(phase.getName());
    }

    @AfterMapping
    protected void map(@MappingTarget Event event, Estimation estimation) {

        event.setUserName(estimation.getUsers()
                .stream()
                .findFirst()
                .map(a -> a.getFirstName() + a.getLastName())
                .orElse(null));
        event.setEstimationName(estimation.getName());
        event.setDate(eventService.getEventCreationDate());
        event.setEstimationName(estimation.getName());
    }
}
