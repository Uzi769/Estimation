package ru.irlix.evaluation.utils.math;

import ru.irlix.evaluation.dao.entity.Task;

import java.util.Map;

public class EstimationRangeCalculator extends Calculable {

    @Override
    public double calcTaskMinHours(Task task) {
        return task.getMinHours() * getRepeatCount(task);
    }

    @Override
    public double calcTaskMaxHours(Task task) {
        return task.getMaxHours() * getRepeatCount(task);
    }

    @Override
    public double calcTaskMinCost(Task task, Map<String, String> request) {
        return calcTaskMinHours(task) * getRoleCost(task, request);
    }

    @Override
    public double calcTaskMaxCost(Task task, Map<String, String> request) {
        return calcTaskMaxHours(task) * getRoleCost(task, request);
    }
}
