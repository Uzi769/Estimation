package ru.irlix.evaluation.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstimationRequest {

    private String name;

    private String client;

    private String description;

    private Integer risk;

    private Long status;

    private String creator;
}
