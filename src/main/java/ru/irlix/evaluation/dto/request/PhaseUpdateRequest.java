package ru.irlix.evaluation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhaseUpdateRequest extends PhaseRequest {
    private Long id;

    @Override
    public String toString() {
        return "PhaseUpdateRequest(" +
                "name=" + getName() +
                ", estimationId=" + getEstimationId() +
                ", sortOrder=" + getSortOrder() +
                ", managementReserve=" + getManagementReserve() +
                ", qaReserve=" + getQaReserve() +
                ", bagsReserve=" + getBagsReserve() +
                ", riskReserve=" + getRiskReserve() +
                ", done=" + getDone() +
                ", managementReserveOn=" + getManagementReserveOn() +
                ", qaReserveOn=" + getQaReserveOn() +
                ", bagsReserveOn=" + getBagsReserveOn() +
                ", riskReserveOn=" + getRiskReserveOn() +
                ')' +
                ", id=" + id;
    }
}
