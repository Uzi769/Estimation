package ru.irlix.evaluation.utils.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.exception.NotFoundException;

@RequiredArgsConstructor
@Component
public class ReportMath {
    private static final int TO_MINUTES = 60;

    public static double calcTaskMaxMinutes(Task task) {
        return task.getHoursMin() * TO_MINUTES * (task.getRepeatCount() != null
                ? task.getRepeatCount()
                : 1);
    }

    public static double calcTaskMinMinutes(Task task) {
        return task.getHoursMin() * TO_MINUTES * (task.getRepeatCount() != null
                ? task.getRepeatCount()
                : 1);
    }

    public static double calcTaskMinCost(Task task, ReportRequest request) {
        return task.getHoursMin() * getRoleCost(task, request);
    }

    public static double calcTaskMaxCost(Task task, ReportRequest request) {
        return task.getHoursMax() * getRoleCost(task, request);
    }

    public static double calcPhaseSummaryMinMinutes(Phase phase) {
        return phase.getTasks().stream()
                .mapToDouble(t -> !t.getTasks().isEmpty()
                        ? t.getTasks().stream()
                        .mapToDouble(ReportMath::calcTaskMinMinutes)
                        .sum()
                        : calcTaskMinMinutes(t)
                )
                .sum();
    }

    public static double calcPhaseSummaryMinCost(Phase phase, ReportRequest request) {
        return phase.getTasks().stream()
                .mapToDouble(t -> !t.getTasks().isEmpty()
                        ? t.getTasks().stream()
                        .mapToDouble(nt -> calcTaskMinCost(nt, request))
                        .sum()
                        : calcTaskMinCost(t, request)
                )
                .sum();
    }

    public static double calcPhaseSummaryMaxMinutes(Phase phase) {
        return phase.getTasks().stream()
                .mapToDouble(t -> !t.getTasks().isEmpty()
                        ? t.getTasks().stream()
                        .mapToDouble(ReportMath::calcTaskMaxMinutes)
                        .sum()
                        : calcTaskMaxMinutes(t)
                )
                .sum();
    }

    public static double calcPhaseSummaryMaxCost(Phase phase, ReportRequest request) {
        return phase.getTasks().stream()
                .mapToDouble(t -> !t.getTasks().isEmpty()
                        ? t.getTasks().stream()
                        .mapToDouble(nt -> calcTaskMaxCost(nt, request))
                        .sum()
                        : calcTaskMaxCost(t, request))
                .sum();
    }

    public static double calcFeatureMinMinutes(Task feature) {
        return feature.getTasks().stream()
                .mapToDouble(ReportMath::calcTaskMinMinutes)
                .sum();
    }

    public static double calcFeatureMinCost(Task feature, ReportRequest request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMinCost(t, request))
                .sum();
    }

    public static double calcFeatureMaxMinutes(Task feature) {
        return feature.getTasks().stream()
                .mapToDouble(ReportMath::calcTaskMaxMinutes)
                .sum();
    }

    public static double calcFeatureMaxCost(Task feature, ReportRequest request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMaxCost(t, request))
                .sum();
    }

    private static double getRoleCost(Task task, ReportRequest request) {
        switch (task.getRole().getValue()) {
            case "analyst":
                return request.getAnalystCost();
            case "developer":
                return request.getDevCost();
            case "designer":
                return request.getDesignCost();
            case "tester":
                return request.getQaCost();
            default:
                throw new NotFoundException("Role with value " + task.getRole().getValue() + " not found");
        }
    }
}
