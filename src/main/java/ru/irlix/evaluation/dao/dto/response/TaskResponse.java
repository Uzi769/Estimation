package ru.irlix.evaluation.dao.dto.response;

import lombok.Data;

@Data
public class TaskResponse {
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
    private PhaseResponse phase;
    private RoleResponse role;
    private Integer parent;
}
