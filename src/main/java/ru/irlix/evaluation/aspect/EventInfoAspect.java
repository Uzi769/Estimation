package ru.irlix.evaluation.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.service.EventService;


@Aspect
@Component
@RequiredArgsConstructor
public class EventInfoAspect {

    private final EventService eventService;

    @AfterReturning(value = "@annotation(ru.irlix.evaluation.aspect.EventInfo)", returning = "value")
    public void eventSaveEstimationAfter(JoinPoint jp, Object value) {
        eventService.createEvent(jp, value);
    }
}
