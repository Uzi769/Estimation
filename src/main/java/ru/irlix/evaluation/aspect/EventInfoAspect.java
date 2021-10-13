package ru.irlix.evaluation.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Event;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.service.EventService;

import java.util.Arrays;


@Aspect
@Component
@RequiredArgsConstructor
public class EventInfoAspect {

    private final EventService eventService;
    private Event eventMediator;


    @After("@annotation(ru.irlix.evaluation.aspect.EventInfo)")
    public void eventSaveEstimationAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        if (methodName.equals("createEstimation")) {
            eventService.createEvent(joinPoint, methodName);
        } else if (methodName.equals("deleteEstimation")) {
            eventService.deleteEvent(eventMediator, methodName, joinPoint);
        }
    }

    @Before("@annotation(ru.irlix.evaluation.aspect.EventInfo)")
    public void eventDeleteEstimationBefore(JoinPoint jp) {
        if (jp.getSignature().getName().equals("deleteEstimation")) {
            Long estimationId = (Long) Arrays.stream(jp.getArgs()).findFirst()
                    .orElseThrow(() -> new NotFoundException("Estimation not found"));
            eventMediator = eventService.saveTheEventBeforeDeletingEstimation(estimationId);
        }
    }

    @After("@annotation(ru.irlix.evaluation.aspect.EventInfo)")
    public void eventSavePhaseAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        if (methodName.equals("createPhase")) {
            eventService.createEvent(joinPoint, methodName);
        } else if (methodName.equals("deletePhase")) {
            eventService.deleteEvent(eventMediator, methodName, joinPoint);
        }
    }


    @Before("@annotation(ru.irlix.evaluation.aspect.EventInfo)")
    public void eventDeletePhaseBefore(JoinPoint joinPoint) {
        if (joinPoint.getSignature().getName().equals("deletePhase")) {
            Long phaseId = (Long) Arrays.stream(joinPoint.getArgs()).findFirst()
                    .orElseThrow(() -> new NotFoundException("Phase not found"));
            eventMediator = eventService.saveTheEventBeforeDeletingPhase(phaseId);
        }
    }

    @After("@annotation(ru.irlix.evaluation.aspect.EventInfo)")
    public void eventSaveTaskAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        if (methodName.equals("createTask")) {
            eventService.createEvent(joinPoint, methodName);
        } else if (methodName.equals("deleteTask")) {
            eventService.deleteEvent(eventMediator, methodName, joinPoint);
        }
    }

    @Before("@annotation(ru.irlix.evaluation.aspect.EventInfo)")
    public void eventDeleteTaskBefore(JoinPoint jp) {
        if (jp.getSignature().getName().equals("deleteTask")) {
            Long taskId = (Long) Arrays.stream(jp.getArgs()).findFirst()
                    .orElseThrow(() -> new NotFoundException("Task not found"));
            eventMediator = eventService.saveTheEventBeforeDeletingTask(taskId);
        }
    }
}
