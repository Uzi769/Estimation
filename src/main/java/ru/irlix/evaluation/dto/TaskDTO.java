package ru.irlix.evaluation.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private Long id;
    private String name;
    private Integer reapitCount;
    private Integer bagsReserve;
    private Integer qaReserve;
    private Integer managementReserve;
    private Integer riskReserve;
    private String comment;
    private Integer hoursMin;
    private Integer hoursMax;
    private PhaseDTO phase;
    private RoleDTO role;
    private Integer parent;
}
