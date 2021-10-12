package ru.irlix.evaluation.dto.request;

import lombok.*;
import ru.irlix.evaluation.utils.marker.OnCreate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PhaseRequest {

    @NotNull(groups = OnCreate.class, message = "{name.notNull}")
    private String name;

    @NotNull(groups = OnCreate.class, message = "{estimationId.notNull}")
    @Positive(message = "{estimationId.positive}")
    private Long estimationId;

    @NotNull(groups = OnCreate.class, message = "{sortOrder.notNull}")
    @PositiveOrZero(message = "{sortOrder.positiveOrZero}")
    private Integer sortOrder;

    private Integer pmReserve;

    private Integer qaReserve;

    private Integer bugsReserve;

    private Integer riskReserve;

    private Boolean done;

    private Boolean pmReserveOn;

    private Boolean qaReserveOn;

    private Boolean bugsReserveOn;

    private Boolean riskReserveOn;
}
