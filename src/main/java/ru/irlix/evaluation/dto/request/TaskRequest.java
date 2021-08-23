package ru.irlix.evaluation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.irlix.evaluation.utils.marker.OnCreate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import ru.irlix.evaluation.utils.EntityConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    private String name;

    @NotNull(groups = OnCreate.class, message = "Phase id is required")
    @Positive(message = "Phase id cant be negative or zero")
    private Long phaseId;

    @Positive(message = "Feature id cant be negative or zero")
    private Long featureId;

    @Positive(message = "Type id cant be negative or zero")
    private Long type = EntityConstants.TASK_ID;

    @Positive(message = "Repeat count cant be negative or zero")
    private Integer repeatCount;

    @Positive(message = "Role id cant be negative or zero")
    private Long roleId;

    private Integer hoursMax;

    private Integer hoursMin;

    private Integer bagsReserve;

    private Integer qaReserve;

    private Integer managementReserve;

    private String comment;

    private Boolean bagsReverseOn;

    private Boolean qaReverseOn;

    private Boolean managementReverseOn;
}
