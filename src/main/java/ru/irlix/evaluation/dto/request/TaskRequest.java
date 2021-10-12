package ru.irlix.evaluation.dto.request;

import lombok.*;
import ru.irlix.evaluation.utils.marker.OnCreate;
import ru.irlix.evaluation.validation.ValidTask;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ValidTask
public class TaskRequest {

    private String name;

    @NotNull(groups = OnCreate.class, message = "{phaseId.notNull}")
    @Positive(message = "{phaseId.positive}")
    private Long phaseId;

    @Positive(message = "{featureId.positive}")
    private Long featureId;

    @Positive(message = "{type.positive}")
    private Long type;

    private Integer repeatCount;

    @Positive(message = "{roleId.positive}")
    private Long roleId;

    private Double minHours;

    private Double maxHours;

    private Integer bugsReserve;

    private Integer qaReserve;

    private Integer pmReserve;

    private String comment;

    private Boolean bugsReserveOn;

    private Boolean qaReserveOn;

    private Boolean pmReserveOn;
}
