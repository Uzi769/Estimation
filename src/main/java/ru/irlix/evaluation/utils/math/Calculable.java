package ru.irlix.evaluation.utils.math;

import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.utils.constant.ReportConstants;
import ru.irlix.evaluation.utils.localization.MessageBundle;

import java.util.Map;
import java.util.ResourceBundle;

public abstract class Calculable {

    private final ResourceBundle messageBundle = MessageBundle.getMessageBundle();

    public abstract double calcTaskMinHours(Task task);

    public abstract double calcTaskMinCost(Task task, Map<String, String> request);

    public abstract double calcTaskMaxHours(Task task);

    public abstract double calcTaskMaxCost(Task task, Map<String, String> request);

    public double calcQaMinHours(Task task) {
        double qaHours = 0;
        if (hasQaAddition(task)) {
            qaHours = calcTaskMinHours(task) * getQaPercent(task);
        }

        return qaHours;
    }

    public double calcQaMaxHours(Task task) {
        double qaHours = 0;
        if (hasQaAddition(task)) {
            qaHours = calcTaskMaxHours(task) * getQaPercent(task);
        }

        return qaHours;
    }

    public double calcPmMinHours(Task task) {
        double pmHours = 0;
        if (hasPmAddition(task)) {
            pmHours = calcTaskMinHours(task) * getPmPercent(task);
        }

        return pmHours;
    }

    public double calcPmMaxHours(Task task) {
        double pmHours = 0;
        if (hasPmAddition(task)) {
            pmHours = calcTaskMaxHours(task) * getPmPercent(task);
        }

        return pmHours;
    }

    public double calcQaMinCost(Task task, Map<String, String> request) {
        return calcQaMinHours(task) * getQaCost(request);
    }

    public double calcQaMaxCost(Task task, Map<String, String> request) {
        return calcQaMaxHours(task) * getQaCost(request);
    }

    public double calcPmMinCost(Task task, Map<String, String> request) {
        return calcPmMinHours(task) * getPmCost(request);
    }

    public double calcPmMaxCost(Task task, Map<String, String> request) {
        return calcPmMaxHours(task) * getPmCost(request);
    }

    protected double getRoleCost(Task task, Map<String, String> request) {
        String roleCost = task.getRole().getValue() + ReportConstants.COST;
        if (!request.containsKey(roleCost)) {
            throw new NotFoundException(roleCost + messageBundle.getString("error.notFound"));
        }

        return Double.parseDouble(request.get(roleCost));
    }

    protected double getRepeatCount(Task task) {
        return task.getRepeatCount() != null
                ? task.getRepeatCount()
                : 1;
    }

    protected boolean hasQaAddition(Task task) {
        return task.getQaReserveOn() != null &&
                task.getQaReserve() != null &&
                task.getQaReserveOn();
    }

    protected double getQaPercent(Task task) {
        double qaPercent = 0;
        if (hasQaAddition(task)) {
            qaPercent = (task.getQaReserve() / 100.0);
        }

        return qaPercent;
    }

    protected double getQaCost(Map<String, String> request) {
        return Double.parseDouble(request.get(ReportConstants.QA_COST));
    }

    protected boolean hasPmAddition(Task task) {
        return task.getPmReserveOn() != null &&
                task.getPmReserve() != null &&
                task.getPmReserveOn();
    }

    protected double getPmPercent(Task task) {
        double pmPercent = 0;
        if (hasPmAddition(task)) {
            pmPercent = (task.getPmReserve() / 100.0);
        }

        return pmPercent;
    }

    protected double getPmCost(Map<String, String> request) {
        return Double.parseDouble(request.get(ReportConstants.PM_COST));
    }
}
