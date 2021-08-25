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

    @NotNull(groups = OnCreate.class, message = "{phaseId.notNull}")
    @Positive(message = "{phaseId.positive}")
    private Long phaseId;

    @Positive(message = "{featureId.positive}")
    private Long featureId;

    @Positive(message = "{type.positive}")
    private Long type = EntityConstants.TASK_ID;

    @Positive(message = "{repeatCount.positive}")
    private Integer repeatCount;

    @Positive(message = "{roleId.positive}")
    private Long roleId;

    private Integer hoursMax;

    private Integer hoursMin;

    private Integer bagsReserve;

    private Integer qaReserve;

    private Integer managementReserve;

    private String comment;

    private Boolean bagsReserveOn;

    private Boolean qaReserveOn;

    private Boolean managementReserveOn;
}
