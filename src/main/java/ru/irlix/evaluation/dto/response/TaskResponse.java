package ru.irlix.evaluation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    private boolean bagsReserveOn;

    private boolean qaReserveOn;

    private boolean managementReserveOn;

    private String comment;

    private Long phaseId;

    private List<TaskResponse> tasks;
}
