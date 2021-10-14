package ru.irlix.evaluation.dto.response;

import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {

    private Long id;

    private String description;

    private Instant date;

    private String userName;
}
