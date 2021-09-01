package ru.irlix.evaluation.utils.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.utils.constant.EntityConstants;

@RequiredArgsConstructor
@Component
public class ReportMath {
    public static double calcTaskMinHours(Task task, ReportRequest request) {
        return request.isPert()
                ? calcTaskMinHoursPert(task)
                : calcTaskMinHoursRange(task);
    }

    public static double calcTaskMinCost(Task task, ReportRequest request) {
        return request.isPert()
                ? calcTaskMinCostPert(task, request)
                : calcTaskMinCostRange(task, request);
    }

    public static double calcTaskMaxHours(Task task, ReportRequest request) {
        return request.isPert()
                ? calcTaskMaxHoursPert(task)
                : calcTaskMaxHoursRange(task);
    }

    public static double calcTaskMaxCost(Task task, ReportRequest request) {
        return request.isPert()
                ? calcTaskMaxCostPert(task, request)
                : calcTaskMaxCostRange(task, request);
    }

    private static double calcTaskMinHoursRange(Task task) {
        return round(task.getHoursMin() * getRepeatCount(task) * getPercentAddition(task));
    }

    private static double calcTaskMaxHoursRange(Task task) {
        return round(task.getHoursMax() * getRepeatCount(task) * getPercentAddition(task));
    }

    private static double calcTaskMinCostRange(Task task, ReportRequest request) {
        return round(calcTaskMinHoursRange(task) * getRoleCost(task, request));
    }

    private static double calcTaskMaxCostRange(Task task, ReportRequest request) {
        return round(calcTaskMaxHoursRange(task) * getRoleCost(task, request));
    }

    private static double calcTaskMinHoursPertWithoutRepeatCount(Task task) {
        return calcTaskMaxHoursPertWithoutRepeatCount(task) - (task.getHoursMax() - task.getHoursMin()) / 6;
    }

    private static double calcTaskMaxHoursPertWithoutRepeatCount(Task task) {
        return (task.getHoursMin() + task.getHoursMax() + 2 * (task.getHoursMin() + task.getHoursMax())) / 6;
    }

    private static double calcTaskMinHoursPert(Task task) {
        return round(calcTaskMinHoursPertWithoutRepeatCount(task) * getRepeatCount(task) * getPercentAddition(task));
    }

    private static double calcTaskMaxHoursPert(Task task) {
        return round(calcTaskMaxHoursPertWithoutRepeatCount(task) * getRepeatCount(task) * getPercentAddition(task));
    }

    private static double calcTaskMinCostPert(Task task, ReportRequest request) {
        return round(calcTaskMinHoursPert(task) * getRoleCost(task, request));
    }

    private static double calcTaskMaxCostPert(Task task, ReportRequest request) {
        return round(calcTaskMaxHoursPert(task) * getRoleCost(task, request));
    }

    private static double getRepeatCount(Task task) {
        return task.getRepeatCount() != null
                ? task.getRepeatCount()
                : 1;
    }

    public static double calcPhaseSummaryMinHours(Phase phase, ReportRequest request) {
        return phase.getTasks().stream()
                .mapToDouble(t -> EntityConstants.FEATURE_ID.equals(t.getType().getId())
                        ? t.getTasks().stream()
                        .mapToDouble(nt -> calcTaskMinHours(nt, request))
                        .sum()
                        : calcTaskMinHours(t, request)
                )
                .sum();
    }

    public static double calcPhaseSummaryMinCost(Phase phase, ReportRequest request) {
        return phase.getTasks().stream()
                .mapToDouble(t -> EntityConstants.FEATURE_ID.equals(t.getType().getId())
                        ? t.getTasks().stream()
                        .mapToDouble(nt -> calcTaskMinCost(nt, request))
                        .sum()
                        : calcTaskMinCost(t, request)
                )
                .sum();
    }

    public static double calcPhaseSummaryMaxHours(Phase phase, ReportRequest request) {
        return phase.getTasks().stream()
                .mapToDouble(t -> EntityConstants.FEATURE_ID.equals(t.getType().getId())
                        ? t.getTasks().stream()
                        .mapToDouble(nt -> calcTaskMaxHours(nt, request))
                        .sum()
                        : calcTaskMaxHours(t, request)
                )
                .sum();
    }

    public static double calcPhaseSummaryMaxCost(Phase phase, ReportRequest request) {
        return phase.getTasks().stream()
                .mapToDouble(t -> EntityConstants.FEATURE_ID.equals(t.getType().getId())
                        ? t.getTasks().stream()
                        .mapToDouble(nt -> calcTaskMaxCost(nt, request))
                        .sum()
                        : calcTaskMaxCost(t, request))
                .sum();
    }

    public static double calcFeatureMinHours(Task feature, ReportRequest request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMinHours(t, request))
                .sum();
    }

    public static double calcFeatureMinCost(Task feature, ReportRequest request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMinCost(t, request))
                .sum();
    }

    public static double calcFeatureMaxHours(Task feature, ReportRequest request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMaxHours(t, request))
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

    private static double getPercentAddition(Task task) {
        double percent = 1;

        if (task.getBagsReserveOn() != null && task.getBagsReserveOn() && task.getBagsReserve() != null) {
            percent *= getPercent(task.getBagsReserve());
        }

        Phase phase = task.getPhase();
        if (phase.getRiskReserveOn() != null && phase.getRiskReserveOn() && phase.getRiskReserve() != null) {
            percent *= getPercent(phase.getRiskReserve());
        }

        Estimation estimation = phase.getEstimation();
        if (estimation.getRisk() != null) {
            percent *= getPercent(estimation.getRisk());
        }

        return percent;
    }

    private static double getPercent(double digit) {
        return 1 + (digit / 100);
    }

    private static double round(double digit) {
        return Math.round(digit * 10) / 10.0;
    }
}
