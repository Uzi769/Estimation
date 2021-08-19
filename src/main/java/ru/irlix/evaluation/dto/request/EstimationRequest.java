package ru.irlix.evaluation.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstimationRequest {
    private String name;
    private String client;
    private String description;
    private String status;
}
