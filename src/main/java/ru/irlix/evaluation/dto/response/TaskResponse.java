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

    private Double minHours;

    private Double maxHours;

    private Long roleId;

    private Long featureId;

    private Integer bugsReserve;

    private Integer qaReserve;

    private Integer pmReserve;

    private boolean bugsReserveOn;

    private boolean qaReserveOn;

    private boolean pmReserveOn;

    private String comment;

    private Long phaseId;

    private List<TaskResponse> tasks;
}
