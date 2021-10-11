package ru.irlix.evaluation.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EstimationResponse {

    private Long id;

    private String name;

    private Instant createDate;

    private String description;

    private Integer risk;

    private String client;

    private String creator;

    private Long status;

    private Double minHours;

    private Double maxHours;

    private List<PhaseResponse> phases;

    private List<UserResponse> users;

    private Map<Long, String> fileMap;
}
