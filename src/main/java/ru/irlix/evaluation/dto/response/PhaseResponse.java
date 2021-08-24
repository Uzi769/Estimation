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

    private List<TaskResponse> tasks;
    
    private Boolean riskReserveOn;
}
