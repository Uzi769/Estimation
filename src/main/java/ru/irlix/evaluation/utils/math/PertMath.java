package ru.irlix.evaluation.utils.math;

import ru.irlix.evaluation.dao.entity.Task;

import java.util.Map;

public class PertMath extends Calculable {

    @Override
    public double calcTaskMinHours(Task task) {
        return round(calcTaskMinHoursWithoutPercents(task) * getPercentAddition(task));
    }

    @Override
    public double calcTaskMinCost(Task task, Map<String, String> request) {
        return round(calcTaskMinHours(task) * getRoleCost(task, request));
    }

    @Override
    public double calcTaskMaxHours(Task task) {
        return round(calcTaskMaxHoursWithoutPercents(task) * getPercentAddition(task));
    }

    @Override
    public double calcTaskMaxCost(Task task, Map<String, String> request) {
        return round(calcTaskMaxHours(task) * getRoleCost(task, request));
    }

    private double calcTaskMinHoursWithoutPercents(Task task) {
        return calcTaskMinHoursPertWithoutRepeatCount(task) * getRepeatCount(task);
    }

    private double calcTaskMaxHoursWithoutPercents(Task task) {
        return calcTaskMaxHoursPertWithoutRepeatCount(task) * getRepeatCount(task);
    }

    private static double calcTaskMinHoursPertWithoutRepeatCount(Task task) {
        return calcTaskMaxHoursPertWithoutRepeatCount(task) - (task.getHoursMax() - task.getHoursMin()) / 6;
    }

    private static double calcTaskMaxHoursPertWithoutRepeatCount(Task task) {
        return (task.getHoursMin() + task.getHoursMax() + 2 * (task.getHoursMin() + task.getHoursMax())) / 6;
    }

    public double calcQaMinHours(Task task) {
        double qaHours = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaHours = round(calcTaskMinHoursWithoutPercents(task) * getQaPercent(task) * getRiskPercent(task));
        }

        return qaHours;
    }

    public double calcQaMinCost(Task task, Map<String, String> request) {
        double qaCost = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaCost = calcQaMinHours(task) * Double.parseDouble(request.get("qaCost"));
        }

        return qaCost;
    }

    public double calcQaMaxHours(Task task) {
        double qaHours = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaHours = round(calcTaskMaxHoursWithoutPercents(task) * getQaPercent(task) * getRiskPercent(task));
        }

        return qaHours;
    }

    public double calcQaMaxCost(Task task, Map<String, String> request) {
        double qaCost = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaCost = calcQaMaxHours(task) * Double.parseDouble(request.get("qaCost"));
        }

        return qaCost;
    }

    public double calcPmMinHours(Task task) {
        double pmHours = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            pmHours = round(calcTaskMinHoursWithoutPercents(task) * getPmPercent(task) * getRiskPercent(task));
        }

        return pmHours;
    }

    public double calcPmMinCost(Task task, Map<String, String> request) {
        double qaCost = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            qaCost = calcPmMinHours(task) * Double.parseDouble(request.get("qaCost"));
        }

        return qaCost;
    }

    public double calcPmMaxHours(Task task) {
        double pmHours = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            pmHours = round(calcTaskMaxHoursWithoutPercents(task) * getPmPercent(task) * getRiskPercent(task));
        }

        return pmHours;
    }

    public double calcPmMaxCost(Task task, Map<String, String> request) {
        double pmCost = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            pmCost = calcPmMaxHours(task) * Double.parseDouble(request.get("pmCost"));
        }

        return pmCost;
    }
}
