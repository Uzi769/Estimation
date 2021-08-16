package ru.irlix.evaluation.dao.dto.response;

import lombok.*;
import ru.irlix.evaluation.dao.entity.Status;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstimationResponse {
    private Long id;
    private String name;
    private Instant createDate;
    private String description;
    private Integer risk;
    private Status status;
    private String client;
    private String creator;
    private List<PhaseResponse> phases;
}
