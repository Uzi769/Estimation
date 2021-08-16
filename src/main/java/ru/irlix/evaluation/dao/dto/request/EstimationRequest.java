package ru.irlix.evaluation.dao.dto.request;

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
}
