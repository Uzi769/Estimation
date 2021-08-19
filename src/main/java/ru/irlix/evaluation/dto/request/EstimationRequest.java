package ru.irlix.evaluation.dto.request;

import lombok.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstimationRequest {

    private String name;

    private String client;

    private String description;

    private Instant createDate;

    private Integer risk;

    private String status;

    private String creator;

    @Valid
    private Set<PhaseRequest> phases;
}
