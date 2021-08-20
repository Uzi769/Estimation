package ru.irlix.evaluation.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhaseRequest {

    @NotNull
    private String name;

    private Long estimationId;

    @NotNull
    private Integer sortOrder;

    private Integer managementReserve;

    private Integer qaReserve;

    private Integer bagsReserve;

    private Integer riskReserve;

    private Boolean done;

    private Boolean managementReserveOn;

    private Boolean qaReserveOn;

    private Boolean bagsReserveOn;
}
