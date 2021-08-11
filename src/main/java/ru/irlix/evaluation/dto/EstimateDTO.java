package ru.irlix.evaluation.dto;

import lombok.Data;
import ru.irlix.evaluation.dao.entity.StatusDictionary;

import java.util.List;

@Data
public class EstimateDTO {
    private Long id;
    private String name;
    private String createDate;
    private String description;
    private Integer risk;
    private StatusDictionary status;
    private String client;
    private String creator;
    private List<PhaseDTO> phases;
}
