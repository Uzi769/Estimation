package ru.irlix.evaluation.utils.math;

import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.utils.constant.EntitiesIdConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EstimationMath {

    private static final PertMath pertMath = new PertMath();
    private static final RangeMath rangeMath = new RangeMath();

    public static double calcTaskMinHours(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcTaskMinHours(task);
    }

    public static double calcTaskMinCost(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcTaskMinCost(task, request);
    }

    public static double calcTaskMaxHours(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcTaskMaxHours(task);
    }

    public static double calcTaskMaxCost(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcTaskMaxCost(task, request);
    }

    public static double calcEstimationMinHours(Estimation estimation, Map<String, String> request) {
        if (request == null) {
            request = new HashMap<>();
            request.put("pert", "false");
        }

        if (estimation.getPhases() == null || estimation.getPhases().isEmpty()) {
            return 0;
        }

        Map<String, String> finalRequest = request;
        return estimation.getPhases().stream()
                .mapToDouble(p -> calcListSummaryMinHours(p.getTasks(), finalRequest))
                .sum();
    }

    public static double calcEstimationMinCost(Estimation estimation, Map<String, String> request) {
        if (estimation.getPhases() == null || estimation.getPhases().isEmpty()) {
            return 0;
        }

        return estimation.getPhases().stream()
                .mapToDouble(p -> calcListSummaryMinCost(p.getTasks(), request))
                .sum();
    }

    public static double calcEstimationMaxHours(Estimation estimation, Map<String, String> request) {
        if (request == null) {
            request = new HashMap<>();
            request.put("pert", "false");
        }

        if (estimation.getPhases() == null || estimation.getPhases().isEmpty()) {
            return 0;
        }

        Map<String, String> finalRequest = request;
        return estimation.getPhases().stream()
                .mapToDouble(p -> calcListSummaryMaxHours(p.getTasks(), finalRequest))
                .sum();
    }

    public static double calcEstimationMaxCost(Estimation estimation, Map<String, String> request) {
        if (estimation.getPhases() == null || estimation.getPhases().isEmpty()) {
            return 0;
        }

        return estimation.getPhases().stream()
                .mapToDouble(p -> calcListSummaryMaxCost(p.getTasks(), request))
                .sum();
    }

    public static double calcListSummaryMinHours(List<Task> tasks, Map<String, String> request) {
        double minHours = calcListSummaryMinHoursWithoutQaAndPm(tasks, request)
                + calcQaSummaryMinHours(tasks, request)
                + calcPmSummaryMinHours(tasks, request);

        return roundToHalf(minHours);
    }

    public static double calcListSummaryMinCost(List<Task> tasks, Map<String, String> request) {
        double minCost = calcListSummaryMinCostWithoutQaAndPm(tasks, request)
                + calcQaSummaryMinCost(tasks, request)
                + calcPmSummaryMinCost(tasks, request);

        return roundToHalf(minCost);
    }

    public static double calcListSummaryMaxHours(List<Task> tasks, Map<String, String> request) {
        double maxHours = calcListSummaryMaxHoursWithoutQaAndPm(tasks, request)
                + calcQaSummaryMaxHours(tasks, request)
                + calcPmSummaryMaxHours(tasks, request);

        return roundToHalf(maxHours);
    }

    public static double calcListSummaryMaxCost(List<Task> tasks, Map<String, String> request) {
        double maxCost = calcListSummaryMaxCostWithoutQaAndPm(tasks, request)
                + calcQaSummaryMaxCost(tasks, request)
                + calcPmSummaryMaxCost(tasks, request);

        return roundToHalf(maxCost);
    }

    public static double calcFeatureMinHoursWithoutQaAndPm(Task feature, Map<String, String> request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMinHours(t, request))
                .sum();
    }

    public static double calcFeatureMinHours(Task feature, Map<String, String> request) {
        return calcFeatureMinHoursWithoutQaAndPm(feature, request)
                + calcQaSummaryMinHours(feature.getTasks(), request)
                + calcPmSummaryMinHours(feature.getTasks(), request);
    }

    public static double calcFeatureMinCostWithoutQaAndPm(Task feature, Map<String, String> request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMinCost(t, request))
                .sum();
    }

    public static double calcFeatureMinCost(Task feature, Map<String, String> request) {
        return calcFeatureMinCostWithoutQaAndPm(feature, request)
                + calcQaSummaryMinCost(feature.getTasks(), request)
                + calcPmSummaryMinCost(feature.getTasks(), request);
    }

    public static double calcFeatureMaxHoursWithoutQaAndPm(Task feature, Map<String, String> request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMaxHours(t, request))
                .sum();
    }

    public static double calcFeatureMaxHours(Task feature, Map<String, String> request) {
        return calcFeatureMaxHoursWithoutQaAndPm(feature, request)
                + calcQaSummaryMaxHours(feature.getTasks(), request)
                + calcPmSummaryMaxHours(feature.getTasks(), request);
    }

    public static double calcFeatureMaxCostWithoutQaAndPm(Task feature, Map<String, String> request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> calcTaskMaxCost(t, request))
                .sum();
    }

    public static double calcFeatureMaxCost(Task feature, Map<String, String> request) {
        return calcFeatureMaxCostWithoutQaAndPm(feature, request)
                + calcQaSummaryMaxCost(feature.getTasks(), request)
                + calcPmSummaryMaxCost(feature.getTasks(), request);
    }

    public static double calcListSummaryMinHoursWithoutQaAndPm(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> EntitiesIdConstants.FEATURE_ID.equals(t.getType().getId())
                        ? calcFeatureMinHours(t, request)
                        : calcTaskMinHours(t, request)
                )
                .sum();
    }

    public static double calcListSummaryMaxHoursWithoutQaAndPm(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> EntitiesIdConstants.FEATURE_ID.equals(t.getType().getId())
                        ? calcFeatureMaxHours(t, request)
                        : calcTaskMaxHours(t, request)
                )
                .sum();
    }

    public static double calcListSummaryMinCostWithoutQaAndPm(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> EntitiesIdConstants.FEATURE_ID.equals(t.getType().getId())
                        ? calcFeatureMinCost(t, request)
                        : calcTaskMinCost(t, request)
                )
                .sum();
    }

    public static double calcListSummaryMaxCostWithoutQaAndPm(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> EntitiesIdConstants.FEATURE_ID.equals(t.getType().getId())
                        ? calcFeatureMaxCost(t, request)
                        : calcTaskMaxCost(t, request)
                )
                .sum();
    }

    public static double calcQaMinHours(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcQaMinHours(task);
    }

    public static double calcQaMinCost(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcQaMinCost(task, request);
    }

    public static double calcQaMaxHours(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcQaMaxHours(task);
    }

    public static double calcQaMaxCost(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcQaMaxCost(task, request);
    }

    public static double calcPmMinHours(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcPmMinHours(task);
    }

    public static double calcPmMinCost(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcPmMinCost(task, request);
    }

    public static double calcPmMaxHours(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcPmMaxHours(task);
    }

    public static double calcPmMaxCost(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcPmMaxCost(task, request);
    }

    public static double calcQaSummaryMinHours(List<Task> tasks, Map<String, String> request) {
        double minHours = tasks.stream()
                .mapToDouble(t -> calcQaMinHours(t, request))
                .sum();

        return Calculable.round(minHours);
    }

    public static double calcQaSummaryMinCost(List<Task> tasks, Map<String, String> request) {
        double minCost = tasks.stream()
                .mapToDouble(t -> calcQaMinCost(t, request))
                .sum();

        return Calculable.round(minCost);
    }

    public static double calcQaSummaryMaxHours(List<Task> tasks, Map<String, String> request) {
        double maxHours = tasks.stream()
                .mapToDouble(t -> calcQaMaxHours(t, request))
                .sum();

        return Calculable.round(maxHours);
    }

    public static double calcQaSummaryMaxCost(List<Task> tasks, Map<String, String> request) {
        double maxCost = tasks.stream()
                .mapToDouble(t -> calcQaMaxCost(t, request))
                .sum();

        return Calculable.round(maxCost);
    }

    public static double calcPmSummaryMinHours(List<Task> tasks, Map<String, String> request) {
        double minHours = tasks.stream()
                .mapToDouble(t -> calcPmMinHours(t, request))
                .sum();

        return Calculable.round(minHours);
    }

    public static double calcPmSummaryMinCost(List<Task> tasks, Map<String, String> request) {
        double minCost = tasks.stream()
                .mapToDouble(t -> calcPmMinCost(t, request))
                .sum();

        return Calculable.round(minCost);
    }

    public static double calcPmSummaryMaxHours(List<Task> tasks, Map<String, String> request) {
        double maxHours = tasks.stream()
                .mapToDouble(t -> calcPmMaxHours(t, request))
                .sum();

        return Calculable.round(maxHours);
    }

    public static double calcPmSummaryMaxCost(List<Task> tasks, Map<String, String> request) {
        double maxCost = tasks.stream()
                .mapToDouble(t -> calcPmMaxCost(t, request))
                .sum();

        return Calculable.round(maxCost);
    }

    public static double calcTaskMinHoursWithQaAndPm(Task task, Map<String, String> request) {
        double minHours = calcTaskMinHours(task, request)
                + calcQaMinHours(task, request)
                + calcPmMinHours(task, request);

        return Calculable.round(minHours);
    }

    public static double calcTaskMinCostWithQaAndPm(Task task, Map<String, String> request) {
        double minCost = calcTaskMinCost(task, request)
                + calcQaMinCost(task, request)
                + calcPmMinCost(task, request);

        return Calculable.round(minCost);
    }

    public static double calcTaskMaxHoursWithQaAndPm(Task task, Map<String, String> request) {
        double maxHours = calcTaskMaxHours(task, request)
                + calcQaMaxHours(task, request)
                + calcPmMaxHours(task, request);

        return Calculable.round(maxHours);
    }

    public static double calcTaskMaxCostWithQaAndPm(Task task, Map<String, String> request) {
        double maxCost = calcTaskMaxCost(task, request)
                + calcQaMaxCost(task, request)
                + calcPmMaxCost(task, request);

        return Calculable.round(maxCost);
    }

    private static Calculable getMath(Map<String, String> request) {
        return Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
    }

    private static double roundToHalf(double value) {
        return Math.ceil(value * 2) / 2.0;
    }
}
