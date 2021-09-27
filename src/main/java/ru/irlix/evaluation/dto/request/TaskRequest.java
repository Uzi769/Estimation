package ru.irlix.evaluation.dto.request;

import lombok.*;
import ru.irlix.evaluation.utils.marker.OnCreate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskRequest {

    private String name;

    @NotNull(groups = OnCreate.class, message = "{phaseId.notNull}")
    @Positive(message = "{phaseId.positive}")
    private Long phaseId;

    @Positive(message = "{featureId.positive}")
    private Long featureId;

    @Positive(message = "{type.positive}")
    private Long type;

    @Positive(message = "{repeatCount.positive}")
    private Integer repeatCount;

    @Positive(message = "{roleId.positive}")
    private Long roleId;

    private Double hoursMax;

    private Double hoursMin;

    private Integer bagsReserve;

    private Integer qaReserve;

    private Integer managementReserve;

    private String comment;

    private Boolean bagsReserveOn;

    private Boolean qaReserveOn;

    private Boolean managementReserveOn;
}
