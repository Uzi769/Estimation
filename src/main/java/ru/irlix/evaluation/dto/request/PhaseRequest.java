package ru.irlix.evaluation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.irlix.evaluation.utils.marker.OnCreate;
import ru.irlix.evaluation.utils.marker.OnUpdate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhaseRequest {

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Name is required")
    private String name;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Estimation id is required")
    @Positive(message = "Estimation id cant be negative or zero")
    private Long estimationId;

    @NotNull(groups = OnCreate.class, message = "Sort order is required")
    @Positive(message = "Sort order cant be negative or zero")
    private Integer sortOrder;

    private Integer managementReserve;

    private Integer qaReserve;

    private Integer bagsReserve;

    private Integer riskReserve;

    private Boolean done;

    private Boolean managementReserveOn;

    private Boolean qaReserveOn;

    private Boolean bagsReserveOn;

    private Boolean riskReserveOn;
}
