package ru.irlix.evaluation.dao.dto.request;

import lombok.*;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstimationFilterRequest {
    private String name;
    private String client;
    private String status;
    private Instant beginDate;
    private Instant endDate;

    private Pageable pageable;
}
