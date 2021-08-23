package ru.irlix.evaluation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.irlix.evaluation.utils.EntityConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    private String name;

    private Long phaseId;

    private Long featureId;

    private Long type = EntityConstants.TASK_ID;

    private Integer repeatCount;

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
