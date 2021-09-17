package ru.irlix.evaluation.utils.math;

import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dto.request.ReportRequest;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.utils.report.enums.EmployeeRole;

import java.util.Locale;

public abstract class Calculable {

    public abstract double calcTaskMinHours(Task task);

    public abstract double calcTaskMinCost(Task task, ReportRequest request);

    public abstract double calcTaskMaxHours(Task task);

    public abstract double calcTaskMaxCost(Task task, ReportRequest request);

    public abstract double calcQaMinHours(Task task);

    public abstract double calcQaMinCost(Task task, ReportRequest request);

    public abstract double calcQaMaxHours(Task task);

    public abstract double calcQaMaxCost(Task task, ReportRequest request);

    public abstract double calcPmMinHours(Task task);

    public abstract double calcPmMinCost(Task task, ReportRequest request);

    public abstract double calcPmMaxHours(Task task);

    public abstract double calcPmMaxCost(Task task, ReportRequest request);

    public static double getPercent(double digit) {
        return 1 + (digit / 100);
    }

    protected static double round(double digit) {
        long valueWithoutRemainder = Math.round(digit * 10) / 10;
        double remainder = Math.round(digit * 10) % 10;
        if ((remainder > 0) && (remainder < 5))
            return valueWithoutRemainder + 0.5;
        if (remainder > 5)
            return valueWithoutRemainder + 1;
        return Math.round(digit * 10) / 10.0;
    }

    protected static double getRoleCost(Task task, ReportRequest request) {
        EmployeeRole role = task.getRole() != null
                ? EmployeeRole.valueOf(task.getRole().getValue().toUpperCase(Locale.ROOT))
                : EmployeeRole.ANALYST;

        switch (role) {
            case ANALYST:
                return request.getAnalystCost();
            case DEVELOPER:
                return request.getDeveloperCost();
            case DESIGNER:
                return request.getDesignerCost();
            case DEVOPS:
                return request.getDevopsCost();
            default:
                throw new NotFoundException("Role with value " + task.getRole().getValue() + " not found");
        }
    }

    protected static double getRepeatCount(Task task) {
        return task.getRepeatCount() != null
                ? task.getRepeatCount()
                : 1;
    }

    protected static double getPercentAddition(Task task) {
        double percent = 1;

        if (task.getBagsReserveOn() != null && task.getBagsReserveOn() && task.getBagsReserve() != null) {
            percent *= getPercent(task.getBagsReserve());
        }

        percent *= getRiskPercent(task);

        return percent;
    }

    protected static double getRiskPercent(Task task) {
        double riskPercent = 1;

        Phase phase = task.getPhase();
        if (phase.getRiskReserveOn() != null && phase.getRiskReserveOn() && phase.getRiskReserve() != null) {
            riskPercent *= getPercent(phase.getRiskReserve());
        }

        Estimation estimation = phase.getEstimation();
        if (estimation.getRisk() != null) {
            riskPercent *= getPercent(estimation.getRisk());
        }

        return riskPercent;
    }

    protected static double getQaPercent(Task task) {
        double qaPercent = 0;
        if (task.getQaReserveOn() != null && task.getQaReserveOn() && task.getQaReserve() != null) {
            qaPercent = (task.getQaReserve() / 100.0);
        }

        return qaPercent;
    }

    protected static double getPmPercent(Task task) {
        double pmPercent = 0;
        if (task.getManagementReserveOn() != null && task.getManagementReserveOn() && task.getManagementReserve() != null) {
            pmPercent = (task.getManagementReserve() / 100.0);
        }

        return pmPercent;
    }
}
