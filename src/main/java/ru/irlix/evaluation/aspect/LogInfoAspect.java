package ru.irlix.evaluation.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Aspect
@Component
public class LogInfoAspect {

    @Before("@annotation(ru.irlix.evaluation.aspect.LogInfo)")
    public void logInputArgsBefore(JoinPoint joinPoint) throws Throwable {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        List<String> argsList = new ArrayList<>();
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            String str = codeSignature.getParameterNames()[i] + ": " + joinPoint.getArgs()[i];
            argsList.add(str);
        }
        log.info(argsList);
    }

    @AfterReturning(value = "@annotation(ru.irlix.evaluation.aspect.LogInfo)", returning = "returnValue")
    public void logReturnValueAfter(JoinPoint joinPoint, Object returnValue) {
        if (returnValue != null) {

            if (returnValue instanceof Page) {
                log.info(((Page<?>) returnValue).getContent());
            } else
                log.info(returnValue.toString());
        } else {
            log.info("return with null as return value");
        }
    }
}
