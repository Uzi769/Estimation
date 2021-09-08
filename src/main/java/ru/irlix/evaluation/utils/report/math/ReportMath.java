package ru.irlix.evaluation.utils.report.math;

import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.utils.constant.EntitiesIdConstants;

import java.util.List;

@Component
public class ReportMath {

    private static final PertMath pertMath = new PertMath();
    private static final RangeMath rangeMath = new RangeMath();

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

    public static double calcListSummaryMinHours(List<Task> tasks, ReportRequest request) {
        return calcListSummaryMinHoursWithoutQaAndPm(tasks, request)
                + calcQaSummaryMinHours(tasks, request)
                + calcPmSummaryMinHours(tasks, request);
    }

    public static double calcListSummaryMinCost(List<Task> tasks, ReportRequest request) {
        return calcListSummaryMinCostWithoutQaAndPm(tasks, request)
                + calcQaSummaryMinCost(tasks, request)
                + calcPmSummaryMinCost(tasks, request);
    }

    public static double calcListSummaryMaxHours(List<Task> tasks, ReportRequest request) {
        return calcListSummaryMaxHoursWithoutQaAndPm(tasks, request)
                + calcQaSummaryMaxHours(tasks, request)
                + calcPmSummaryMaxHours(tasks, request);
    }

    public static double calcListSummaryMaxCost(List<Task> tasks, ReportRequest request) {
        return calcListSummaryMaxCostWithoutQaAndPm(tasks, request)
                + calcQaSummaryMaxCost(tasks, request)
                + calcPmSummaryMaxCost(tasks, request);
    }

    public static double calcFeatureMinHoursWithoutQaAndPm(Task feature, ReportRequest request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMinHours(t, request))
                .sum();
    }

    public static double calcFeatureMinHours(Task feature, ReportRequest request) {
        return calcFeatureMinHoursWithoutQaAndPm(feature, request)
                + calcQaSummaryMinHours(feature.getTasks(), request)
                + calcPmSummaryMinHours(feature.getTasks(), request);
    }

    public static double calcFeatureMinCostWithoutQaAndPm(Task feature, ReportRequest request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMinCost(t, request))
                .sum();
    }

    public static double calcFeatureMinCost(Task feature, ReportRequest request) {
        return calcFeatureMinCostWithoutQaAndPm(feature, request)
                + calcQaSummaryMinCost(feature.getTasks(), request)
                + calcPmSummaryMinCost(feature.getTasks(), request);
    }

    public static double calcFeatureMaxHoursWithoutQaAndPm(Task feature, ReportRequest request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMaxHours(t, request))
                .sum();
    }

    public static double calcFeatureMaxHours(Task feature, ReportRequest request) {
        return calcFeatureMaxHoursWithoutQaAndPm(feature, request)
                + calcQaSummaryMaxHours(feature.getTasks(), request)
                + calcPmSummaryMaxHours(feature.getTasks(), request);
    }

    public static double calcFeatureMaxCostWithoutQaAndPm(Task feature, ReportRequest request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMaxCost(t, request))
                .sum();
    }

    public static double calcFeatureMaxCost(Task feature, ReportRequest request) {
        return calcFeatureMaxCostWithoutQaAndPm(feature, request)
                + calcQaSummaryMaxCost(feature.getTasks(), request)
                + calcPmSummaryMaxCost(feature.getTasks(), request);
    }

    public static double calcListSummaryMinHoursWithoutQaAndPm(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> EntitiesIdConstants.FEATURE_ID.equals(t.getType().getId())
                        ? calcFeatureMinHours(t, request)
                        : calcTaskMinHours(t, request)
                )
                .sum();
    }

    public static double calcListSummaryMaxHoursWithoutQaAndPm(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> EntitiesIdConstants.FEATURE_ID.equals(t.getType().getId())
                        ? calcFeatureMaxHours(t, request)
                        : calcTaskMaxHours(t, request)
                )
                .sum();
    }

    public static double calcListSummaryMinCostWithoutQaAndPm(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> EntitiesIdConstants.FEATURE_ID.equals(t.getType().getId())
                        ? calcFeatureMinCost(t, request)
                        : calcTaskMinCost(t, request)
                )
                .sum();
    }

    public static double calcListSummaryMaxCostWithoutQaAndPm(List<Task> tasks, ReportRequest request) {
        return tasks.stream()
                .mapToDouble(t -> EntitiesIdConstants.FEATURE_ID.equals(t.getType().getId())
                        ? calcFeatureMaxCost(t, request)
                        : calcTaskMaxCost(t, request)
                )
                .sum();
    }

    public static double calcQaMinHours(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcQaMinHours(task);
    }

    public static double calcQaMinCost(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcQaMinCost(task, request);
    }

    public static double calcQaMaxHours(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcQaMaxHours(task);
    }

    public static double calcQaMaxCost(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcQaMaxCost(task, request);
    }

    public static double calcPmMinHours(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcPmMinHours(task);
    }

    public static double calcPmMinCost(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcPmMinCost(task, request);
    }

    public static double calcPmMaxHours(Task task, ReportRequest request) {
        Calculable math = request.isPert() ? pertMath : rangeMath;
        return math.calcPmMaxHours(task);
    }

    public static double calcPmMaxCost(Task task, ReportRequest request) {
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

    public static double calcTaskMinHoursWithQaAndPm(Task task, ReportRequest request) {
        return calcTaskMinHours(task, request)
                + calcQaMinHours(task, request)
                + calcPmMinHours(task, request);
    }

    public static double calcTaskMinCostWithQaAndPm(Task task, ReportRequest request) {
        return calcTaskMinCost(task, request)
                + calcQaMinCost(task, request)
                + calcPmMinCost(task, request);
    }

    public static double calcTaskMaxHoursWithQaAndPm(Task task, ReportRequest request) {
        return calcTaskMaxHours(task, request)
                + calcQaMaxHours(task, request)
                + calcPmMaxHours(task, request);
    }

    public static double calcTaskMaxCostWithQaAndPm(Task task, ReportRequest request) {
        return calcTaskMaxCost(task, request)
                + calcQaMaxCost(task, request)
                + calcPmMaxCost(task, request);
    }
}
