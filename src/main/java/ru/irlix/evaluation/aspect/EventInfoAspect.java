package ru.irlix.evaluation.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.service.EventService;


@Aspect
@Component
@RequiredArgsConstructor
public class EventInfoAspect {

    private final EventService eventService;

    @AfterReturning(value = "@annotation(ru.irlix.evaluation.aspect.EventInfo)", returning = "value")
    public void makeSavingEvent(JoinPoint jp, Object value) {
        String methodName = jp.getSignature().getName();
        if (methodName.contains("create")) {
            eventService.createEvent(jp, value);
        }
    }

    @Around(value = "@annotation(ru.irlix.evaluation.aspect.EventInfo)")
    public Object makeDeletingEvent(ProceedingJoinPoint jp) throws Throwable {
        String methodName = jp.getSignature().getName();
        if (!methodName.contains("delete")) {
            return jp.proceed();
        }

        Object elementToDelete = eventService.getElementToDelete(jp);
        Object proceed = jp.proceed();
        eventService.createEvent(jp, elementToDelete);
        return proceed;
    }
}
