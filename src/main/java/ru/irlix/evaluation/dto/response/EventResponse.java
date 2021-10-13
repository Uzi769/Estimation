package ru.irlix.evaluation.dto.response;

import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {

    private String description;

    private Instant date;

    private String userName;

    private String target;

}
