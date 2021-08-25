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

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "{name.notNull}")
    private String name;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "{estimationId.notNull}")
    @Positive(message = "estimationId.positive")
    private Long estimationId;

    @NotNull(groups = OnCreate.class, message = "{sortOrder.notNull}")
    @Positive(message = "{sortOrder.positive}")
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
