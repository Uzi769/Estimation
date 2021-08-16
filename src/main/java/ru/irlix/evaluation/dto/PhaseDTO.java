package ru.irlix.evaluation.dto;

import lombok.Data;

import java.util.List;

@Data
public class PhaseDTO {
    private Long id;
    private String name;
    private EstimateDTO estimate;
    private Integer sortOrder;
    private Integer managementReserve;
    private Integer bagsReserve;
    private Integer qaReserve;
    private Integer riskReserve;
    private RoleDTO role;
    private List<TaskDTO> tasks;
    private Boolean done;
}
