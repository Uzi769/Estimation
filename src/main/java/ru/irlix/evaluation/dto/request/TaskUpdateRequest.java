package ru.irlix.evaluation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateRequest extends TaskRequest {
    private Long id;

    @Override
    public String toString() {
        return "TaskUpdateRequest(" +
                "name='" + getName() + '\'' +
                ", phaseId=" + getPhaseId() +
                ", featureId=" + getFeatureId() +
                ", type=" + getType() +
                ", repeatCount=" + getRepeatCount() +
                ", roleId=" + getRoleId() +
                ", hoursMax=" + getHoursMax() +
                ", hoursMin=" + getHoursMin() +
                ", bagsReserve=" + getBagsReserve() +
                ", qaReserve=" + getQaReserve() +
                ", managementReserve=" + getManagementReserve() +
                ", comment='" + getComment() + '\'' +
                ", bagsReserveOn=" + getBagsReserveOn() +
                ", qaReserveOn=" + getQaReserveOn() +
                ", managementReserveOn=" + getManagementReserveOn() +
                ", id=" + id + ')';
    }
}
