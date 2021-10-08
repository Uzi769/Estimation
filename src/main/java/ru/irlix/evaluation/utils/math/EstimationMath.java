package ru.irlix.evaluation.utils.math;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.response.EstimationCostResponse;
import ru.irlix.evaluation.dto.response.EstimationStatsResponse;
import ru.irlix.evaluation.dto.response.PhaseStatsResponse;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EstimationMath {

    private final EstimationCalculator calculator;


    public double getEstimationMinHours(Estimation estimation, Map<String, String> request) {
        return roundToHalf(calculator.getEstimationMinHoursWithAdditions(estimation, request));
    }

    public double getEstimationMaxHours(Estimation estimation, Map<String, String> request) {
        return roundToHalf(calculator.getEstimationMaxHoursWithAdditions(estimation, request));
    }

    public double getEstimationMinCost(Estimation estimation, Map<String, String> request) {
        return roundToHalf(calculator.getEstimationMinCostWithAdditions(estimation, request));
    }

    public double getEstimationMaxCost(Estimation estimation, Map<String, String> request) {
        return roundToHalf(calculator.getEstimationMaxCostWithAdditions(estimation, request));
    }


    public double getListSummaryMinHoursWithoutQaAndPm(List<Task> tasks, Map<String, String> request) {
        return round(calculator.getListMinHours(tasks, request));
    }

    public double getListSummaryMaxHoursWithoutQaAndPm(List<Task> tasks, Map<String, String> request) {
        return round(calculator.getListMaxHours(tasks, request));
    }

    public double getListSummaryMinCostWithoutQaAndPm(List<Task> tasks, Map<String, String> request) {
        return round(calculator.getListMinCost(tasks, request));
    }

    public double getListSummaryMaxCostWithoutQaAndPm(List<Task> tasks, Map<String, String> request) {
        return round(calculator.getListMaxCost(tasks, request));
    }


    public double getListSummaryMinHours(List<Task> tasks, Map<String, String> request) {
        return roundToHalf(calculator.getListMinHoursWithAdditions(tasks, request));
    }

    public double getListSummaryMaxHours(List<Task> tasks, Map<String, String> request) {
        return roundToHalf(calculator.getListMaxHoursWithAdditions(tasks, request));
    }

    public double getListSummaryMinCost(List<Task> tasks, Map<String, String> request) {
        return roundToHalf(calculator.getListMinCostWithAdditions(tasks, request));
    }

    public double getListSummaryMaxCost(List<Task> tasks, Map<String, String> request) {
        return roundToHalf(calculator.getListMaxCostWithAdditions(tasks, request));
    }


    public double getTaskMinHoursWithoutQaAndPm(Task task, Map<String, String> request) {
        return round(calculator.getTaskMinHoursWithBugfixAndRisk(task, request));
    }

    public double getTaskMaxHoursWithoutQaAndPm(Task task, Map<String, String> request) {
        return round(calculator.getTaskMaxHoursWithBugfixAndRisk(task, request));
    }

    public double getTaskMinCostWithoutQaAndPm(Task task, Map<String, String> request) {
        return round(calculator.getTaskMinCostWithBugfixAndRisk(task, request));
    }

    public double getTaskMaxCostWithoutQaAndPm(Task task, Map<String, String> request) {
        return round(calculator.getTaskMaxCostWithBugfixAndRisk(task, request));
    }


    public double getTaskMinHours(Task task, Map<String, String> request) {
        return round(calculator.getTaskMinHoursWithAdditions(task, request));
    }

    public double getTaskMaxHours(Task task, Map<String, String> request) {
        return round(calculator.getTaskMaxHoursWithAdditions(task, request));
    }

    public double getTaskMinCost(Task task, Map<String, String> request) {
        return round(calculator.getTaskMinCostWithAdditions(task, request));
    }

    public double getTaskMaxCost(Task task, Map<String, String> request) {
        return round(calculator.getTaskMaxCostWithAdditions(task, request));
    }


    public double getFeatureMinHours(Task feature, Map<String, String> request) {
        return round(calculator.getFeatureMinHoursWithAdditions(feature, request));
    }

    public double getFeatureMaxHours(Task feature, Map<String, String> request) {
        return round(calculator.getFeatureMaxHoursWithAdditions(feature, request));
    }

    public double getFeatureMinCost(Task feature, Map<String, String> request) {
        return round(calculator.getFeatureMinCostWithAdditions(feature, request));
    }

    public double getFeatureMaxCost(Task feature, Map<String, String> request) {
        return round(calculator.getFeatureMaxCostWithAdditions(feature, request));
    }


    public double getQaSummaryMinHours(List<Task> tasks, Map<String, String> request) {
        return round(calculator.getQaMinHoursSummaryWithRisk(tasks, request));
    }

    public double getQaSummaryMaxHours(List<Task> tasks, Map<String, String> request) {
        return round(calculator.getQaMaxHoursSummaryWithRisk(tasks, request));
    }

    public double getQaSummaryMinCost(List<Task> tasks, Map<String, String> request) {
        return round(calculator.getQaMinCostSummaryWithRisk(tasks, request));
    }

    public double getQaSummaryMaxCost(List<Task> tasks, Map<String, String> request) {
        return round(calculator.getQaMaxCostSummaryWithRisk(tasks, request));
    }


    public double getPmSummaryMinHours(List<Task> tasks, Map<String, String> request) {
        return round(calculator.getPmMinHoursSummaryWithRisk(tasks, request));
    }

    public double getPmSummaryMaxHours(List<Task> tasks, Map<String, String> request) {
        return round(calculator.getPmMaxHoursSummaryWithRisk(tasks, request));
    }

    public double getPmSummaryMinCost(List<Task> tasks, Map<String, String> request) {
        return round(calculator.getPmMinCostSummaryWithRisk(tasks, request));
    }

    public double getPmSummaryMaxCost(List<Task> tasks, Map<String, String> request) {
        return round(calculator.getPmMaxCostSummaryWithRisk(tasks, request));
    }


    public double getQaMinHours(Task task, Map<String, String> request) {
        return round(calculator.getQaMinHoursWithRisk(task, request));
    }

    public double getQaMaxHours(Task task, Map<String, String> request) {
        return round(calculator.getQaMaxHoursWithRisk(task, request));
    }

    public double getQaMinCost(Task task, Map<String, String> request) {
        return round(calculator.getQaMinCostWithRisk(task, request));
    }

    public double getQaMaxCost(Task task, Map<String, String> request) {
        return round(calculator.getQaMaxCostWithRisk(task, request));
    }


    public double getPmMinHours(Task task, Map<String, String> request) {
        return round(calculator.getPmMinHoursWithRisk(task, request));
    }

    public double getPmMaxHours(Task task, Map<String, String> request) {
        return round(calculator.getPmMaxHoursWithRisk(task, request));
    }

    public double getPmMinCost(Task task, Map<String, String> request) {
        return round(calculator.getPmMinCostWithRisk(task, request));
    }

    public double getPmMaxCost(Task task, Map<String, String> request) {
        return round(calculator.getPmMaxCostWithRisk(task, request));
    }

    public List<PhaseStatsResponse> getPhaseStats(Phase phase) {
        List<PhaseStatsResponse> stats = calculator.getPhaseStats(phase);
        stats.forEach(this::roundPhaseStats);

        return stats;
    }

    public List<EstimationStatsResponse> getEstimationStats(Estimation estimation) {
        List<EstimationStatsResponse> stats = calculator.getEstimationStats(estimation);
        if (stats.size() > 0) {
            stats.stream()
                    .limit(stats.size() - 1)
                    .forEach(this::roundEstimationStats);

            roundEstimationStatResult(stats.get(stats.size() - 1));
        }

        return stats;
    }

    public EstimationCostResponse getEstimationCost(Estimation estimation, Map<String, String> request) {
        return new EstimationCostResponse(
                getEstimationMinCost(estimation, request),
                getEstimationMaxCost(estimation, request)
        );
    }

    private void roundPhaseStats(PhaseStatsResponse stats) {
        stats.setMinHours(round(stats.getMinHours()));
        stats.setMaxHours(round(stats.getMaxHours()));

        stats.setBugfixMinHours(round(stats.getBugfixMinHours()));
        stats.setBugfixMaxHours(round(stats.getBugfixMaxHours()));

        stats.setQaMinHours(round(stats.getQaMinHours()));
        stats.setQaMaxHours(round(stats.getQaMaxHours()));

        stats.setPmMinHours(round(stats.getPmMinHours()));
        stats.setPmMaxHours(round(stats.getPmMaxHours()));

        stats.setSumMinHours(roundToHalf(stats.getSumMinHours()));
        stats.setSumMaxHours(roundToHalf(stats.getSumMaxHours()));
    }

    private void roundEstimationStats(EstimationStatsResponse estimationMath) {
        estimationMath.setMinHoursRange(round(estimationMath.getMinHoursRange()));
        estimationMath.setMaxHoursRange(round(estimationMath.getMaxHoursRange()));

        estimationMath.setMinHoursPert(round(estimationMath.getMinHoursPert()));
        estimationMath.setMaxHoursPert(round(estimationMath.getMaxHoursPert()));
    }

    private void roundEstimationStatResult(EstimationStatsResponse estimationMath) {
        estimationMath.setMinHoursRange(roundToHalf(estimationMath.getMinHoursRange()));
        estimationMath.setMaxHoursRange(roundToHalf(estimationMath.getMaxHoursRange()));

        estimationMath.setMinHoursPert(roundToHalf(estimationMath.getMinHoursPert()));
        estimationMath.setMaxHoursPert(roundToHalf(estimationMath.getMaxHoursPert()));
    }

    public Map<Role, List<Task>> getRolesMap(Estimation estimation) {
        return calculator.getRolesMap(estimation);
    }

    public boolean isFeature(Task task) {
        return calculator.isFeature(task);
    }

    public double roundToHalf(double value) {
        return Math.round(value * 2) / 2.0;
    }

    public double round(double value) {
        return Math.round(value * 10) / 10.0;
    }
}
