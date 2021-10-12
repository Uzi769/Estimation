package ru.irlix.evaluation.utils.math;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.Phase;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dao.entity.Task;
import ru.irlix.evaluation.dao.helper.RoleHelper;
import ru.irlix.evaluation.dao.mapper.RoleMapper;
import ru.irlix.evaluation.dto.response.EstimationStatsResponse;
import ru.irlix.evaluation.dto.response.PhaseStatsResponse;
import ru.irlix.evaluation.dto.response.RoleResponse;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.utils.constant.EntitiesIdConstants;
import ru.irlix.evaluation.utils.constant.ReportConstants;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class EstimationCalculator {

    private final EstimationPertCalculator pertMath;
    private final EstimationRangeCalculator rangeMath;
    private final RoleMapper roleMapper;
    private final RoleHelper roleHelper;

    private final Map<String, String> rangeRequest;
    private final Map<String, String> pertRequest;

    private List<Role> optionalRoles;

    @Autowired
    public EstimationCalculator(RoleMapper roleMapper, RoleHelper roleHelper) {
        pertMath = new EstimationPertCalculator();
        rangeMath = new EstimationRangeCalculator();
        this.roleMapper = roleMapper;
        this.roleHelper = roleHelper;

        rangeRequest = new HashMap<>();
        rangeRequest.put("pert", "false");

        pertRequest = new HashMap<>();
        pertRequest.put("pert", "true");
    }

    private double getTaskMinHours(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcTaskMinHours(task);
    }

    private double getTaskMaxHours(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcTaskMaxHours(task);
    }

    private double getTaskMinCost(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcTaskMinCost(task, request);
    }

    private double getTaskMaxCost(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcTaskMaxCost(task, request);
    }

    public double getTaskMinHoursWithBugfixAndRisk(Task task, Map<String, String> request) {
        return getTaskMinHours(task, request) * calcBugfixAndRiskAddition(task);
    }

    public double getTaskMaxHoursWithBugfixAndRisk(Task task, Map<String, String> request) {
        return getTaskMaxHours(task, request) * calcBugfixAndRiskAddition(task);
    }

    public double getTaskMinCostWithBugfixAndRisk(Task task, Map<String, String> request) {
        return getTaskMinCost(task, request) * calcBugfixAndRiskAddition(task);
    }

    public double getTaskMaxCostWithBugfixAndRisk(Task task, Map<String, String> request) {
        return getTaskMaxCost(task, request) * calcBugfixAndRiskAddition(task);
    }

    private double calcBugfixAndRiskAddition(Task task) {
        return (1 + getBugfixPercent(task)) * getRiskAddition(task);
    }

    public double getTaskMinHoursWithAdditions(Task task, Map<String, String> request) {
        return getTaskMinHours(task, request) * calcFullAddition(task, request);
    }

    public double getTaskMaxHoursWithAdditions(Task task, Map<String, String> request) {
        return getTaskMaxHours(task, request) * calcFullAddition(task, request);
    }

    public double getTaskMinCostWithAdditions(Task task, Map<String, String> request) {
        return getTaskMinCostWithBugfixAndRisk(task, request)
                + getQaMinCostWithRisk(task, request)
                + getPmMinCostWithRisk(task, request);
    }

    public double getTaskMaxCostWithAdditions(Task task, Map<String, String> request) {
        return getTaskMaxCostWithBugfixAndRisk(task, request)
                + getQaMaxCostWithRisk(task, request)
                + getPmMaxCostWithRisk(task, request);
    }

    private double calcFullAddition(Task task, Map<String, String> request) {
        return (1 + getBugfixPercent(task) + getQaPercent(task, request) + getPmPercent(task, request))
                * getRiskAddition(task);
    }

    private double getQaMinHours(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcQaMinHours(task);
    }

    private double getQaMaxHours(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcQaMaxHours(task);
    }

    private double getQaMinCost(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcQaMinCost(task, request);
    }

    private double getQaMaxCost(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcQaMaxCost(task, request);
    }

    public double getQaMinHoursWithRisk(Task task, Map<String, String> request) {
        return getRiskAddition(task) * getQaMinHours(task, request);
    }

    public double getQaMaxHoursWithRisk(Task task, Map<String, String> request) {
        return getRiskAddition(task) * getQaMaxHours(task, request);
    }

    public double getQaMinCostWithRisk(Task task, Map<String, String> request) {
        return getRiskAddition(task) * getQaMinCost(task, request);
    }

    public double getQaMaxCostWithRisk(Task task, Map<String, String> request) {
        return getRiskAddition(task) * getQaMaxCost(task, request);
    }

    public double getQaMinHoursSummaryWithRisk(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> getQaMinHoursWithRisk(t, request))
                .sum();
    }

    public double getQaMaxHoursSummaryWithRisk(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> getQaMaxHoursWithRisk(t, request))
                .sum();
    }

    public double getQaMinCostSummaryWithRisk(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> getQaMinCostWithRisk(t, request))
                .sum();
    }

    public double getQaMaxCostSummaryWithRisk(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> getQaMaxCostWithRisk(t, request))
                .sum();
    }

    private double getQaPercent(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.getQaPercent(task);
    }

    private double getPmMinHours(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcPmMinHours(task);
    }

    private double getPmMaxHours(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcPmMaxHours(task);
    }

    private double getPmMinCost(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcPmMinCost(task, request);
    }

    private double getPmMaxCost(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.calcPmMaxCost(task, request);
    }

    public double getPmMinHoursWithRisk(Task task, Map<String, String> request) {
        return getRiskAddition(task) * getPmMinHours(task, request);
    }

    public double getPmMaxHoursWithRisk(Task task, Map<String, String> request) {
        return getRiskAddition(task) * getPmMaxHours(task, request);
    }

    public double getPmMinCostWithRisk(Task task, Map<String, String> request) {
        return getRiskAddition(task) * getPmMinCost(task, request);
    }

    public double getPmMaxCostWithRisk(Task task, Map<String, String> request) {
        return getRiskAddition(task) * getPmMaxCost(task, request);
    }

    public double getPmMinHoursSummaryWithRisk(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> getPmMinHoursWithRisk(t, request))
                .sum();
    }

    public double getPmMaxHoursSummaryWithRisk(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> getPmMaxHoursWithRisk(t, request))
                .sum();
    }

    public double getPmMinCostSummaryWithRisk(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> getPmMinCostWithRisk(t, request))
                .sum();
    }

    public double getPmMaxCostSummaryWithRisk(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> getPmMaxCostWithRisk(t, request))
                .sum();
    }

    private double getPmPercent(Task task, Map<String, String> request) {
        Calculable math = getMath(request);
        return math.getPmPercent(task);
    }

    private double getFeatureMinHours(Task feature, Map<String, String> request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> getTaskMinHoursWithBugfixAndRisk(t, request))
                .sum();
    }

    private double getFeatureMaxHours(Task feature, Map<String, String> request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> getTaskMaxHoursWithBugfixAndRisk(t, request))
                .sum();
    }

    private double getFeatureMinCost(Task feature, Map<String, String> request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> getTaskMinCostWithBugfixAndRisk(t, request))
                .sum();
    }

    private double getFeatureMaxCost(Task feature, Map<String, String> request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> getTaskMaxCostWithBugfixAndRisk(t, request))
                .sum();
    }

    public double getFeatureMinHoursWithAdditions(Task feature, Map<String, String> request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> getTaskMinHoursWithAdditions(t, request))
                .sum();
    }

    public double getFeatureMaxHoursWithAdditions(Task feature, Map<String, String> request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> getTaskMaxHoursWithAdditions(t, request))
                .sum();
    }

    public double getFeatureMinCostWithAdditions(Task feature, Map<String, String> request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> getTaskMinCostWithAdditions(t, request))
                .sum();
    }

    public double getFeatureMaxCostWithAdditions(Task feature, Map<String, String> request) {
        return feature.getTasks().stream()
                .mapToDouble(t -> getTaskMaxCostWithAdditions(t, request))
                .sum();
    }


    public double getListMinHours(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> isFeature(t)
                        ? getFeatureMinHours(t, request)
                        : getTaskMinHoursWithBugfixAndRisk(t, request))
                .sum();
    }

    public double getListMaxHours(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> isFeature(t)
                        ? getFeatureMaxHours(t, request)
                        : getTaskMaxHoursWithBugfixAndRisk(t, request))
                .sum();
    }

    public double getListMinCost(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> isFeature(t)
                        ? getFeatureMinCost(t, request)
                        : getTaskMinCostWithBugfixAndRisk(t, request))
                .sum();
    }

    public double getListMaxCost(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> isFeature(t)
                        ? getFeatureMaxCost(t, request)
                        : getTaskMaxCostWithBugfixAndRisk(t, request))
                .sum();
    }


    public double getListMinHoursWithAdditions(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> isFeature(t)
                        ? getFeatureMinHoursWithAdditions(t, request)
                        : getTaskMinHoursWithAdditions(t, request))
                .sum();
    }

    public double getListMaxHoursWithAdditions(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> isFeature(t)
                        ? getFeatureMaxHoursWithAdditions(t, request)
                        : getTaskMaxHoursWithAdditions(t, request))
                .sum();
    }

    public double getListMinCostWithAdditions(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> isFeature(t)
                        ? getFeatureMinCostWithAdditions(t, request)
                        : getTaskMinCostWithAdditions(t, request))
                .sum();
    }

    public double getListMaxCostWithAdditions(List<Task> tasks, Map<String, String> request) {
        return tasks.stream()
                .mapToDouble(t -> isFeature(t)
                        ? getFeatureMaxCostWithAdditions(t, request)
                        : getTaskMaxCostWithAdditions(t, request))
                .sum();
    }


    public double getEstimationMinHoursWithAdditions(Estimation estimation, Map<String, String> request) {
        Map<String, String> finalRequest = Optional.ofNullable(request).map(r -> request)
                .orElse(rangeRequest);

        if (estimation.getPhases() == null || estimation.getPhases().isEmpty()) {
            return 0;
        }

        return estimation.getPhases().stream()
                .mapToDouble(p -> getListMinHoursWithAdditions(p.getTasks(), finalRequest))
                .sum();
    }

    public double getEstimationMaxHoursWithAdditions(Estimation estimation, Map<String, String> request) {
        Map<String, String> finalRequest = Optional.ofNullable(request).map(r -> request)
                .orElse(rangeRequest);

        if (estimation.getPhases() == null || estimation.getPhases().isEmpty()) {
            return 0;
        }

        return estimation.getPhases().stream()
                .mapToDouble(p -> getListMaxHoursWithAdditions(p.getTasks(), finalRequest))
                .sum();
    }

    public double getEstimationMinCostWithAdditions(Estimation estimation, Map<String, String> request) {
        return estimation.getPhases().stream()
                .mapToDouble(p -> getListMinCostWithAdditions(p.getTasks(), request))
                .sum();
    }

    public double getEstimationMaxCostWithAdditions(Estimation estimation, Map<String, String> request) {
        return estimation.getPhases().stream()
                .mapToDouble(p -> getListMaxCostWithAdditions(p.getTasks(), request))
                .sum();
    }

    public List<PhaseStatsResponse> getPhaseStats(Phase phase) {
        setOptionalRoles();
        List<Task> allTasks = new ArrayList<>();
        addPhaseTasksToList(phase, allTasks);

        Map<Role, List<Task>> rolesMap = allTasks.stream()
                .collect(Collectors.groupingBy(Task::getRole));

        List<PhaseStatsResponse> stats = rolesMap.entrySet().stream()
                .map(this::mapToPhaseMathStats)
                .collect(Collectors.toList());

        double minHoursSummary = 0;
        double maxHoursSummary = 0;
        double bugfixMinHoursSummary = 0;
        double bugfixMaxHoursSummary = 0;
        double qaMinHoursSummary = 0;
        double qaMaxHoursSummary = 0;
        double pmMinHoursSummary = 0;
        double pmMaxHoursSummary = 0;

        if (!stats.isEmpty()) {
            for (PhaseStatsResponse phaseStat : stats) {
                minHoursSummary += phaseStat.getMinHours();
                maxHoursSummary += phaseStat.getMaxHours();

                bugfixMinHoursSummary += phaseStat.getBugsMinHours();
                bugfixMaxHoursSummary += phaseStat.getBugsMaxHours();

                qaMinHoursSummary += phaseStat.getQaMinHours();
                qaMaxHoursSummary += phaseStat.getQaMaxHours();

                pmMinHoursSummary += phaseStat.getPmMinHours();
                pmMaxHoursSummary += phaseStat.getPmMaxHours();
            }

            double minHoursResult = minHoursSummary + qaMinHoursSummary + bugfixMinHoursSummary + pmMinHoursSummary;
            double maxHoursResult = maxHoursSummary + qaMaxHoursSummary + bugfixMaxHoursSummary + pmMaxHoursSummary;

            stats.add(new PhaseStatsResponse(
                    getRole(ReportConstants.SUM_ROLE),
                    minHoursSummary, maxHoursSummary,
                    bugfixMinHoursSummary, bugfixMaxHoursSummary,
                    qaMinHoursSummary, qaMaxHoursSummary,
                    pmMinHoursSummary, pmMaxHoursSummary,
                    minHoursResult, maxHoursResult
            ));
        }

        return stats;
    }

    private void addPhaseTasksToList(Phase phase, List<Task> allTasks) {
        phase.getTasks().forEach(t -> addFeatureTasksIfExist(allTasks, t));
    }

    private PhaseStatsResponse mapToPhaseMathStats(Map.Entry<Role, List<Task>> entry) {
        double minHours = 0;
        double maxHours = 0;
        double bugfixMinHours = 0;
        double bugfixMaxHours = 0;
        double qaMinHours = 0;
        double qaMaxHours = 0;
        double pmMinHours = 0;
        double pmMaxHours = 0;

        Map<String, String> request = new HashMap<>();
        request.put("pert", "false");

        for (Task task : entry.getValue()) {
            minHours += task.getMinHours();
            maxHours += task.getMaxHours();

            bugfixMinHours += task.getMinHours() * getBugfixPercent(task);
            bugfixMaxHours += task.getMaxHours() * getBugfixPercent(task);

            qaMinHours += task.getMinHours() * getQaPercent(task, request);
            qaMaxHours += task.getMaxHours() * getQaPercent(task, request);

            pmMinHours += task.getMinHours() * getPmPercent(task, request);
            pmMaxHours += task.getMaxHours() * getPmPercent(task, request);
        }

        double minHoursSummary = minHours + qaMinHours + bugfixMinHours + pmMinHours;
        double maxHoursSummary = maxHours + qaMaxHours + bugfixMaxHours + pmMaxHours;
        Phase phase = entry.getValue().get(0).getPhase();

        return new PhaseStatsResponse(
                roleMapper.roleToRoleResponse(entry.getKey()),
                minHours * getRiskOnPhase(phase),
                maxHours * getRiskOnPhase(phase),
                bugfixMinHours * getRiskOnPhase(phase),
                bugfixMaxHours * getRiskOnPhase(phase),
                qaMinHours * getRiskOnPhase(phase),
                qaMaxHours * getRiskOnPhase(phase),
                pmMinHours * getRiskOnPhase(phase),
                pmMaxHours * getRiskOnPhase(phase),
                minHoursSummary * getRiskOnPhase(phase),
                maxHoursSummary * getRiskOnPhase(phase)
        );
    }

    public List<EstimationStatsResponse> getEstimationStats(Estimation estimation) {
        setOptionalRoles();
        List<Task> allTasks = new ArrayList<>();
        estimation.getPhases().forEach(p -> addPhaseTasksToList(p, allTasks));

        Map<Role, List<Task>> rolesMap = allTasks.stream()
                .collect(Collectors.groupingBy(Task::getRole));

        List<EstimationStatsResponse> stats = rolesMap.entrySet().stream()
                .map(this::mapToEstimationMath)
                .collect(Collectors.toList());

        double qaMaxHours = getQaMaxHoursSummaryWithRisk(allTasks, rangeRequest);
        if (qaMaxHours > 0) {
            stats.add(new EstimationStatsResponse(
                    getRole(ReportConstants.QA_ROLE),
                    getQaMinHoursSummaryWithRisk(allTasks, rangeRequest),
                    getQaMaxHoursSummaryWithRisk(allTasks, rangeRequest),
                    getQaMinHoursSummaryWithRisk(allTasks, pertRequest),
                    getQaMaxHoursSummaryWithRisk(allTasks, pertRequest)
            ));
        }

        double pmMaxHours = getPmMaxHoursSummaryWithRisk(allTasks, rangeRequest);
        if (pmMaxHours > 0) {
            stats.add(new EstimationStatsResponse(
                    getRole(ReportConstants.PM_ROLE),
                    getPmMinHoursSummaryWithRisk(allTasks, rangeRequest),
                    getPmMaxHoursSummaryWithRisk(allTasks, rangeRequest),
                    getPmMinHoursSummaryWithRisk(allTasks, pertRequest),
                    getPmMaxHoursSummaryWithRisk(allTasks, pertRequest)
            ));
        }

        if (!stats.isEmpty()) {
            double minHoursRangeSummary = 0;
            double maxHoursRangeSummary = 0;
            double minHoursPertSummary = 0;
            double maxHoursPertSummary = 0;

            for (EstimationStatsResponse response : stats) {
                minHoursRangeSummary += response.getMinHoursRange();
                maxHoursRangeSummary += response.getMaxHoursRange();
                minHoursPertSummary += response.getMinHoursPert();
                maxHoursPertSummary += response.getMaxHoursPert();
            }

            stats.add(new EstimationStatsResponse(
                    getRole(ReportConstants.SUM_ROLE),
                    minHoursRangeSummary,
                    maxHoursRangeSummary,
                    minHoursPertSummary,
                    maxHoursPertSummary
            ));
        }

        return stats;
    }

    private EstimationStatsResponse mapToEstimationMath(Map.Entry<Role, List<Task>> entry) {
        return new EstimationStatsResponse(
                roleMapper.roleToRoleResponse(entry.getKey()),
                getListMinHours(entry.getValue(), rangeRequest),
                getListMaxHours(entry.getValue(), rangeRequest),
                getListMinHours(entry.getValue(), pertRequest),
                getListMaxHours(entry.getValue(), pertRequest)
        );
    }

    private double getPercent(double digit) {
        return digit / 100;
    }

    private boolean hasBugfixAddition(Task task) {
        return task.getBugsReserveOn() != null &&
                task.getBugsReserve() != null &&
                task.getBugsReserveOn();
    }

    private boolean hasRiskAddition(Phase phase) {
        return phase.getRiskReserveOn() != null &&
                phase.getRiskReserve() != null &&
                phase.getRiskReserveOn();
    }

    private double getBugfixPercent(Task task) {
        if (!hasBugfixAddition(task)) {
            return 0;
        }

        return getPercent(task.getBugsReserve());
    }

    public Map<Role, List<Task>> getRolesMap(Estimation estimation) {
        List<Task> allTasks = new ArrayList<>();
        estimation.getPhases().stream()
                .flatMap(p -> p.getTasks().stream())
                .forEach(t -> addFeatureTasksIfExist(allTasks, t));

        return allTasks.stream()
                .collect(Collectors.groupingBy(Task::getRole));
    }

    private void addFeatureTasksIfExist(List<Task> allTasks, Task task) {
        if (isFeature(task)) {
            List<Task> tasks = task.getTasks().stream()
                    .filter(this::isRequiredTime)
                    .collect(Collectors.toList());
            allTasks.addAll(tasks);
        } else if (isRequiredTime(task)) {
            allTasks.add(task);
        }
    }

    private boolean isRequiredTime(Task task) {
        return task.getMaxHours() > 0;
    }

    private double getRiskOnPhase(Phase phase) {
        double riskPercent = 1;
        if (hasRiskAddition(phase)) {
            riskPercent *= 1 + getPercent(phase.getRiskReserve());
        }

        return riskPercent;
    }

    private double getRiskAddition(Task task) {
        Phase phase = task.getPhase();
        double riskPercent = getRiskOnPhase(phase);

        Estimation estimation = phase.getEstimation();
        if (estimation.getRisk() != null) {
            riskPercent *= 1 + getPercent(estimation.getRisk());
        }

        return riskPercent;
    }

    public boolean isFeature(Task task) {
        return EntitiesIdConstants.FEATURE_ID.equals(task.getType().getId());
    }

    private Calculable getMath(Map<String, String> request) {
        return Boolean.parseBoolean(request.get("pert")) ? pertMath : rangeMath;
    }

    private RoleResponse getRole(String roleValue) {
        Role role = optionalRoles.stream()
                .filter(r -> r.getValue().equals(roleValue))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Role " + roleValue + " not found"));

        return roleMapper.roleToRoleResponse(role);
    }

    private void setOptionalRoles() {
        optionalRoles = roleHelper.findOptionalRoles();
    }
}
