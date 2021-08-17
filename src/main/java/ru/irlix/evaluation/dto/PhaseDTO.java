package ru.irlix.evaluation.dto;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
public class PhaseDTO {
    private String name;
    private Integer sortOrder;
    private Integer managementReserve;
    private Integer bagsReserve;
    private Integer qaReserve;
    private Integer riskReserve;
    private List<TaskDTO> tasks;
    private Boolean done;
}
