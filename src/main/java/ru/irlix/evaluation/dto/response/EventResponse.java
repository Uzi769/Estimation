package ru.irlix.evaluation.dto.response;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

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
//
//    private String namePhase;
//
//    private String nameTask;
}
