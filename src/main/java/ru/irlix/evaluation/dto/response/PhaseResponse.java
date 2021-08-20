package ru.irlix.evaluation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhaseResponse {

    private Long id;

    private String name;

    private Long estimationId;

    private Integer sortOrder;

    private Integer managementReserve;

    private Integer bagsReserve;

    private Integer qaReserve;

    private Integer riskReserve;

    private Boolean done;

    private Boolean managementReserveOn;

    private Boolean qaReserveOn;

    private Boolean bagsReserveOn;

    private Boolean riskReserveOn;
}
