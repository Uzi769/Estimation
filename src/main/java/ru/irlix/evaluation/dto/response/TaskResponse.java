package ru.irlix.evaluation.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskResponse {

    private Long id;

    private String name;

    private Long type;

    private Integer repeatCount;

    private Double hoursMin;

    private Double hoursMax;

    private Long roleId;

    private Long parentId;

    private Integer bagsReserve;

    private Integer qaReserve;

    private Integer managementReserve;

    private boolean bagsReserveOn;

    private boolean qaReserveOn;

    private boolean managementReserveOn;

    private String comment;

    private Long phaseId;

    private List<TaskResponse> tasks;
}
