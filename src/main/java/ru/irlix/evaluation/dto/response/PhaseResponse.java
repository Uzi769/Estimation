package ru.irlix.evaluation.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PhaseResponse {

    private Long id;

    private String name;

    private Long estimationId;

    private Integer sortOrder;

    private Integer pmReserve;

    private Integer bugsReserve;

    private Integer qaReserve;

    private Integer riskReserve;

    private Boolean done;

    private Boolean pmReserveOn;

    private Boolean qaReserveOn;

    private Boolean bugsReserveOn;

    private List<TaskResponse> tasks;

    private Boolean riskReserveOn;
}
