package ru.irlix.evaluation.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private Long id;
    private String value;
    private String displayValue;
    private List<PhaseDTO> phases;
    private List<TaskDTO> tasks;
}
