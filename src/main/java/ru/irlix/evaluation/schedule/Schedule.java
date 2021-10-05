package ru.irlix.evaluation.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.service.KeycloakService;

@Component
@RequiredArgsConstructor
public class Schedule {

    private final KeycloakService keycloakService;

    @Scheduled(cron = "0 0 1 * * *")
    public void schedule() {
        keycloakService.update();
    }
}
