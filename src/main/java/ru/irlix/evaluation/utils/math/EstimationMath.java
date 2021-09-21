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
        Calculable math = Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
        return math.calcTaskMinHours(task);
    }

    public static double calcTaskMinCost(Task task, Map<String, String> request) {
        Calculable math = Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
        return math.calcTaskMinCost(task, request);
    }

    public static double calcTaskMaxHours(Task task, Map<String, String> request) {
        Calculable math = Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
        return math.calcTaskMaxHours(task);
    }

    public static double calcTaskMaxCost(Task task, Map<String, String> request) {
        Calculable math = Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
        return math.calcTaskMaxCost(task, request);
    }

    public static double calcEstimationMinHours(Estimation estimation) {
        Map<String, String> request = new HashMap<>();
        request.put("pert", "false");

        if (estimation.getPhases() == null || estimation.getPhases().isEmpty()) {
            return 0;
        }

        return estimation.getPhases().stream()
                .mapToDouble(p -> calcListSummaryMinHours(p.getTasks(), request))
                .sum();
    }

    public static double calcEstimationMaxHours(Estimation estimation) {
        Map<String, String> request = new HashMap<>();
        request.put("pert", "false");

        if (estimation.getPhases() == null || estimation.getPhases().isEmpty()) {
            return 0;
        }

        return estimation.getPhases().stream()
                .mapToDouble(p -> calcListSummaryMaxHours(p.getTasks(), request))
                .sum();
    }

    public static double calcListSummaryMinHours(List<Task> tasks, Map<String, String> request) {
        return calcListSummaryMinHoursWithoutQaAndPm(tasks, request)
                + calcQaSummaryMinHours(tasks, request)
                + calcPmSummaryMinHours(tasks, request);
    }

    public static double calcListSummaryMinCost(List<Task> tasks, Map<String, String> request) {
        return calcListSummaryMinCostWithoutQaAndPm(tasks, request)
                + calcQaSummaryMinCost(tasks, request)
                + calcPmSummaryMinCost(tasks, request);
    }

    public static double calcListSummaryMaxHours(List<Task> tasks, Map<String, String> request) {
        return calcListSummaryMaxHoursWithoutQaAndPm(tasks, request)
                + calcQaSummaryMaxHours(tasks, request)
                + calcPmSummaryMaxHours(tasks, request);
    }

    public static double calcListSummaryMaxCost(List<Task> tasks, Map<String, String> request) {
        return calcListSummaryMaxCostWithoutQaAndPm(tasks, request)
                + calcQaSummaryMaxCost(tasks, request)
                + calcPmSummaryMaxCost(tasks, request);
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
        Calculable math = Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
        return math.calcQaMinHours(task);
    }

    public static double calcQaMinCost(Task task, Map<String, String> request) {
        Calculable math = Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
        return math.calcQaMinCost(task, request);
    }

    public static double calcQaMaxHours(Task task, Map<String, String> request) {
        Calculable math = Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
        return math.calcQaMaxHours(task);
    }

    public static double calcQaMaxCost(Task task, Map<String, String> request) {
        Calculable math = Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
        return math.calcQaMaxCost(task, request);
    }

    public static double calcPmMinHours(Task task, Map<String, String> request) {
        Calculable math = Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
        return math.calcPmMinHours(task);
    }

    public static double calcPmMinCost(Task task, Map<String, String> request) {
        Calculable math = Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
        return math.calcPmMinCost(task, request);
    }

    public static double calcPmMaxHours(Task task, Map<String, String> request) {
        Calculable math = Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
        return math.calcPmMaxHours(task);
    }

    public static double calcPmMaxCost(Task task, Map<String, String> request) {
        Calculable math = Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
        return math.calcPmMaxCost(task, request);
    }

    public static double calcQaSummaryMinHours(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> calcQaMinHours(t, request))
                .sum();
    }

    public static double calcQaSummaryMinCost(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> calcQaMinCost(t, request))
                .sum();
    }

    public static double calcQaSummaryMaxHours(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> calcQaMaxHours(t, request))
                .sum();
    }

    public static double calcQaSummaryMaxCost(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> calcQaMaxCost(t, request))
                .sum();
    }

    public static double calcPmSummaryMinHours(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> calcPmMinHours(t, request))
                .sum();
    }

    public static double calcPmSummaryMinCost(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> calcPmMinCost(t, request))
                .sum();
    }

    public static double calcPmSummaryMaxHours(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> calcPmMaxHours(t, request))
                .sum();
    }

    public static double calcPmSummaryMaxCost(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> calcPmMaxCost(t, request))
                .sum();
    }

    public static double calcTaskMinHoursWithQaAndPm(Task task, Map<String, String> request) {
        return calcTaskMinHours(task, request)
                + calcQaMinHours(task, request)
                + calcPmMinHours(task, request);
    }

    public static double calcTaskMinCostWithQaAndPm(Task task, Map<String, String> request) {
        return calcTaskMinCost(task, request)
                + calcQaMinCost(task, request)
                + calcPmMinCost(task, request);
    }

    public static double calcTaskMaxHoursWithQaAndPm(Task task, Map<String, String> request) {
        return calcTaskMaxHours(task, request)
                + calcQaMaxHours(task, request)
                + calcPmMaxHours(task, request);
    }

    public static double calcTaskMaxCostWithQaAndPm(Task task, Map<String, String> request) {
        return calcTaskMaxCost(task, request)
                + calcQaMaxCost(task, request)
                + calcPmMaxCost(task, request);
    }
}
