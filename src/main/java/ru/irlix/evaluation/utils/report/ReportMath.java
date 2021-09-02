package ru.irlix.evaluation.utils.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.utils.constant.EntityConstants;

import java.util.List;

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
        return calcListSummaryMinHours(phase.getTasks(), request);
    }

    public static double calcPhaseSummaryMinCost(Phase phase, ReportRequest request) {
        return calcListSummaryMinCost(phase.getTasks(), request);
    }

    public static double calcPhaseSummaryMaxHours(Phase phase, ReportRequest request) {
        return calcListSummaryMaxHours(phase.getTasks(), request);
    }

    public static double calcPhaseSummaryMaxCost(Phase phase, ReportRequest request) {
        return calcListSummaryMaxCost(phase.getTasks(), request);
    }

    public static double calcFeatureMinHours(Task feature, ReportRequest request) {
        double featureMinHours = feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMinHours(t, request))
                .sum();

        return featureMinHours + calcQaSummaryMinHours(feature.getTasks())
                + calcPmSummaryMinHours(feature.getTasks());
    }

    public static double calcFeatureMinCost(Task feature, ReportRequest request) {
        double featureMinCost = feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMinCost(t, request))
                .sum();

        return featureMinCost + calcQaSummaryMinCost(feature.getTasks(), request)
                + calcPmSummaryMinCost(feature.getTasks(), request);
    }

    public static double calcFeatureMaxHours(Task feature, ReportRequest request) {
        double featureMaxHours = feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMaxHours(t, request))
                .sum();

        return featureMaxHours + calcQaSummaryMaxHours(feature.getTasks())
                + calcPmSummaryMaxHours(feature.getTasks());
    }

    public static double calcFeatureMaxCost(Task feature, ReportRequest request) {
        double featureMaxCost = feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMaxCost(t, request))
                .sum();

        return featureMaxCost + calcQaSummaryMaxCost(feature.getTasks(), request)
                + calcPmSummaryMaxCost(feature.getTasks(), request);
    }

    public static double calcListSummaryMinHours(List<Task> tasks, ReportRequest request) {
        double tasksMinHours = tasks.stream()
                .mapToDouble(t -> EntityConstants.FEATURE_ID.equals(t.getType().getId())
                        ? calcFeatureMinHours(t, request)
                        : calcTaskMinHours(t, request)
                )
                .sum();

        return tasksMinHours + calcQaSummaryMinHours(tasks) + calcPmSummaryMinHours(tasks);
    }

    public static double calcListSummaryMaxHours(List<Task> tasks, ReportRequest request) {
        double tasksMaxHours = tasks.stream()
                .mapToDouble(t -> EntityConstants.FEATURE_ID.equals(t.getType().getId())
                        ? calcFeatureMaxHours(t, request)
                        : calcTaskMaxHours(t, request)
                )
                .sum();

        return tasksMaxHours + calcQaSummaryMaxHours(tasks) + calcPmSummaryMaxHours(tasks);
    }

    public static double calcListSummaryMinCost(List<Task> tasks, ReportRequest request) {
        double tasksMinCost = tasks.stream()
                .mapToDouble(t -> EntityConstants.FEATURE_ID.equals(t.getType().getId())
                        ? calcFeatureMinCost(t, request)
                        : calcTaskMinCost(t, request)
                )
                .sum();

        return tasksMinCost + calcQaSummaryMinCost(tasks, request) + calcPmSummaryMinCost(tasks, request);
    }

    public static double calcListSummaryMaxCost(List<Task> tasks, ReportRequest request) {
        double tasksMaxCost = tasks.stream()
                .mapToDouble(t -> EntityConstants.FEATURE_ID.equals(t.getType().getId())
                        ? calcFeatureMaxCost(t, request)
                        : calcTaskMaxCost(t, request)
                )
                .sum();

        return tasksMaxCost + calcQaSummaryMaxCost(tasks, request) + calcPmSummaryMaxCost(tasks, request);
    }

    private static double calcQaMinHours(Task task) {
        double qaHours = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaHours = round(task.getHoursMin() * getRepeatCount(task) * getQaPercent(task) * getRiskPercent(task));
        }

        return qaHours;
    }

    private static double calcQaMinCost(Task task, ReportRequest request) {
        double qaCost = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaCost = calcQaMinHours(task) * request.getQaCost();
        }

        return qaCost;
    }

    private static double calcQaMaxHours(Task task) {
        double qaHours = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaHours = round(task.getHoursMax() * getRepeatCount(task) * getQaPercent(task) * getRiskPercent(task));
        }

        return qaHours;
    }

    private static double calcQaMaxCost(Task task, ReportRequest request) {
        double qaCost = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaCost = calcQaMaxHours(task) * request.getQaCost();
        }

        return qaCost;
    }

    private static double calcPmMinHours(Task task) {
        double pmHours = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            pmHours = round(task.getHoursMin() * getRepeatCount(task) * getPmPercent(task) * getRiskPercent(task));
        }

        return pmHours;
    }

    private static double calcPmMinCost(Task task, ReportRequest request) {
        double qaCost = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            qaCost = calcPmMinHours(task) * request.getQaCost();
        }

        return qaCost;
    }

    private static double calcPmMaxHours(Task task) {
        double pmHours = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            pmHours = round(task.getHoursMax() * getRepeatCount(task) * getPmPercent(task) * getRiskPercent(task));
        }

        return pmHours;
    }

    private static double calcPmMaxCost(Task task, ReportRequest request) {
        double pmCost = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            pmCost = calcPmMaxHours(task) * request.getPmCost();
        }

        return pmCost;
    }

    public static double calcQaSummaryMinHours(List<Task> tasks) {
        return tasks.stream()
                .mapToDouble(ReportMath::calcQaMinHours)
                .sum();
    }

    public static double calcQaSummaryMinCost(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> calcQaMinCost(t, request))
                .sum();
    }

    public static double calcQaSummaryMaxHours(List<Task> tasks) {
        return tasks.stream()
                .mapToDouble(ReportMath::calcQaMaxHours)
                .sum();
    }

    public static double calcQaSummaryMaxCost(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> calcQaMaxCost(t, request))
                .sum();
    }

    public static double calcPmSummaryMinHours(List<Task> tasks) {
        return tasks.stream()
                .mapToDouble(ReportMath::calcPmMinHours)
                .sum();
    }

    public static double calcPmSummaryMinCost(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> calcPmMinCost(t, request))
                .sum();
    }

    public static double calcPmSummaryMaxHours(List<Task> tasks) {
        return tasks.stream()
                .mapToDouble(ReportMath::calcPmMaxHours)
                .sum();
    }

    public static double calcPmSummaryMaxCost(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> calcPmMaxCost(t, request))
                .sum();
    }

    private static double getRoleCost(Task task, ReportRequest request) {
        String roleValue = task.getRole() != null
                ? task.getRole().getValue()
                : "analyst";

        switch (roleValue) {
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

        percent *= getRiskPercent(task);

        return percent;
    }

    private static double getRiskPercent(Task task) {
        double riskPercent = 1;

        Phase phase = task.getPhase();
        if (phase.getRiskReserveOn() != null && phase.getRiskReserveOn() && phase.getRiskReserve() != null) {
            riskPercent *= getPercent(phase.getRiskReserve());
        }

        Estimation estimation = phase.getEstimation();
        if (estimation.getRisk() != null) {
            riskPercent *= getPercent(estimation.getRisk());
        }

        return riskPercent;
    }

    private static double getQaPercent(Task task) {
        double qaPercent = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaPercent = round(task.getQaReserve() / 100.0);
        }

        return qaPercent;
    }

    private static double getPmPercent(Task task) {
        double pmPercent = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            pmPercent = round(task.getManagementReserve() / 100.0);
        }

        return pmPercent;
    }

    private static double getPercent(double digit) {
        return 1 + (digit / 100);
    }

    private static double round(double digit) {
        return Math.round(digit * 10) / 10.0;
    }
}
