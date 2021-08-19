package ru.irlix.evaluation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {

    private Long id;

    private String name;

    private Integer repeatCount;

    private Integer bagsReserve;

    private Integer qaReserve;

    private Integer managementReserve;

    private Integer riskReserve;

    private String comment;

    private Integer hoursMin;

    private Integer hoursMax;

    private Long phase;

    private Long role;

    private Long parent;
}
