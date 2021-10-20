package ru.irlix.evaluation.dto.request;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class EventFilterRequest extends PageableAndSortableRequest {

    private String text;

    private String description;

    private String estimationName;

    private String phaseName;

    private String taskName;

    private Long estimationId;

    private Long phaseId;

    private Long taskId;

    private Long actionId;

    private String username;

    private Instant beginDate;

    private Instant endDate;
}
