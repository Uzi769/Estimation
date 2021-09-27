package ru.irlix.evaluation.utils.math;

import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.utils.constant.ReportConstants;

import java.util.Map;

public class RangeMath extends Calculable {

    @Override
    public double calcTaskMinHours(Task task) {
        return round(task.getHoursMin() * getRepeatCount(task) * getPercentAddition(task));
    }

    @Override
    public double calcTaskMinCost(Task task, Map<String, String> request) {
        return round(calcTaskMinHours(task) * getRoleCost(task, request));
    }

    @Override
    public double calcTaskMaxHours(Task task) {
        return round(task.getHoursMax() * getRepeatCount(task) * getPercentAddition(task));
    }

    @Override
    public double calcTaskMaxCost(Task task, Map<String, String> request) {
        return round(calcTaskMaxHours(task) * getRoleCost(task, request));
    }

    public double calcQaMinHours(Task task) {
        double qaHours = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaHours = task.getHoursMin() * getRepeatCount(task) * getQaPercent(task) * getRiskPercent(task);
        }

        return qaHours;
    }

    public double calcQaMinCost(Task task, Map<String, String> request) {
        double qaCost = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaCost = calcQaMinHours(task) * Double.parseDouble(request.get(ReportConstants.QA_COST));
        }

        return qaCost;
    }

    public double calcQaMaxHours(Task task) {
        double qaHours = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaHours = task.getHoursMax() * getRepeatCount(task) * getQaPercent(task) * getRiskPercent(task);
        }

        return qaHours;
    }

    public double calcQaMaxCost(Task task, Map<String, String> request) {
        double qaCost = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaCost = calcQaMaxHours(task) * Double.parseDouble(request.get(ReportConstants.QA_COST));
        }

        return qaCost;
    }

    public double calcPmMinHours(Task task) {
        double pmHours = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            pmHours = task.getHoursMin() * getRepeatCount(task) * getPmPercent(task) * getRiskPercent(task);
        }

        return pmHours;
    }

    public double calcPmMinCost(Task task, Map<String, String> request) {
        double pmCost = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            pmCost = calcPmMinHours(task) * Double.parseDouble(request.get(ReportConstants.PM_COST));
        }

        return pmCost;
    }

    public double calcPmMaxHours(Task task) {
        double pmHours = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            pmHours = round(task.getHoursMax() * getRepeatCount(task) * getPmPercent(task) * getRiskPercent(task));
        }

        return pmHours;
    }

    public double calcPmMaxCost(Task task, Map<String, String> request) {
        double pmCost = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            pmCost = calcPmMaxHours(task) * Double.parseDouble(request.get(ReportConstants.PM_COST));
        }

        return pmCost;
    }
}
