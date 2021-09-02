package ru.irlix.evaluation.utils.report.math;

import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.constant.EntityConstants;

import java.util.List;

@Component
public class ReportMath {

    private static final PertMath pertMath;
    private static final RangeMath rangeMath;

    static {
        pertMath = new PertMath();
        rangeMath = new RangeMath();
    }

    public static double calcTaskMinHours(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcTaskMinHours(task);
    }

    public static double calcTaskMinCost(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcTaskMinCost(task, request);
    }

    public static double calcTaskMaxHours(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcTaskMaxHours(task);
    }

    public static double calcTaskMaxCost(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcTaskMaxCost(task, request);
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

        return featureMinHours + calcQaSummaryMinHours(feature.getTasks(), request)
                + calcPmSummaryMinHours(feature.getTasks(), request);
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

        return featureMaxHours + calcQaSummaryMaxHours(feature.getTasks(), request)
                + calcPmSummaryMaxHours(feature.getTasks(), request);
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

        return tasksMinHours + calcQaSummaryMinHours(tasks, request) + calcPmSummaryMinHours(tasks, request);
    }

    public static double calcListSummaryMaxHours(List<Task> tasks, ReportRequest request) {
        double tasksMaxHours = tasks.stream()
                .mapToDouble(t -> EntityConstants.FEATURE_ID.equals(t.getType().getId())
                        ? calcFeatureMaxHours(t, request)
                        : calcTaskMaxHours(t, request)
                )
                .sum();

        return tasksMaxHours + calcQaSummaryMaxHours(tasks, request) + calcPmSummaryMaxHours(tasks, request);
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

    private static double calcQaMinHours(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcQaMinHours(task);
    }

    private static double calcQaMinCost(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcQaMinCost(task, request);
    }

    private static double calcQaMaxHours(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcQaMaxHours(task);
    }

    private static double calcQaMaxCost(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcQaMaxCost(task, request);
    }

    private static double calcPmMinHours(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcPmMinHours(task);
    }

    private static double calcPmMinCost(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcPmMinCost(task, request);
    }

    private static double calcPmMaxHours(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcPmMaxHours(task);
    }

    private static double calcPmMaxCost(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcPmMaxCost(task, request);
    }

    public static double calcQaSummaryMinHours(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> calcQaMinHours(t, request))
                .sum();
    }

    public static double calcQaSummaryMinCost(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> calcQaMinCost(t, request))
                .sum();
    }

    public static double calcQaSummaryMaxHours(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> calcQaMaxHours(t, request))
                .sum();
    }

    public static double calcQaSummaryMaxCost(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> calcQaMaxCost(t, request))
                .sum();
    }

    public static double calcPmSummaryMinHours(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> calcPmMinHours(t, request))
                .sum();
    }

    public static double calcPmSummaryMinCost(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> calcPmMinCost(t, request))
                .sum();
    }

    public static double calcPmSummaryMaxHours(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> calcPmMaxHours(t, request))
                .sum();
    }

    public static double calcPmSummaryMaxCost(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> calcPmMaxCost(t, request))
                .sum();
    }
}
