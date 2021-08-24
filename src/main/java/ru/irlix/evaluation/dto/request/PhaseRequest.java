package ru.irlix.evaluation.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhaseRequest {

    public interface New {
    }

    public interface Update {
    }

    @NotNull(groups = {New.class, Update.class}, message = "{name.notEmpty}")
    private String name;

    @NotNull(groups = {New.class, Update.class})
    private Long estimationId;

    @NotNull(groups = New.class)
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
