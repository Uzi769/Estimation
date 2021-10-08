package ru.irlix.evaluation.utils.math;

import ru.irlix.evaluation.dao.entity.Task;

import java.util.Map;

public class EstimationPertCalculator extends Calculable {

    @Override
    public double calcTaskMinHours(Task task) {
        return calcTaskMinHoursPertWithoutRepeatCount(task) * getRepeatCount(task);
    }

    @Override
    public double calcTaskMaxHours(Task task) {
        return calcTaskMaxHoursPertWithoutRepeatCount(task) * getRepeatCount(task);
    }

    @Override
    public double calcTaskMinCost(Task task, Map<String, String> request) {
        return calcTaskMinHours(task) * getRoleCost(task, request);
    }

    @Override
    public double calcTaskMaxCost(Task task, Map<String, String> request) {
        return calcTaskMaxHours(task) * getRoleCost(task, request);
    }

    private double calcTaskMinHoursPertWithoutRepeatCount(Task task) {
        return calcTaskMaxHoursPertWithoutRepeatCount(task) - (task.getHoursMax() - task.getHoursMin()) / 6;
    }

    private double calcTaskMaxHoursPertWithoutRepeatCount(Task task) {
        return (task.getHoursMin() + task.getHoursMax() + 2 * (task.getHoursMin() + task.getHoursMax())) / 6;
    }
}
