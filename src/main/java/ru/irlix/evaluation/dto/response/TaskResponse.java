package ru.irlix.evaluation.dto.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private Long id;

    private String name;

    private Long type;

    private Integer repeatCount;

    private Integer hoursMin;

    private Integer hoursMax;

    private Long roleId;

    private Long parentId;

    private Integer bagsReserve;

    private Integer qaReserve;

    private Integer managementReserve;

    private boolean bagsReverseOn;

    private boolean qaReverseOn;

    private boolean managementReverseOn;

    private String comment;

    private Long phaseId;

    private Set<TaskResponse> tasks;
}
