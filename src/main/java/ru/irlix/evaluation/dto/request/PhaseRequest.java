package ru.irlix.evaluation.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhaseRequest {

    @NotBlank
    private String name;

    private Long estimationId;

    @NotBlank
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
