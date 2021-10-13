package ru.irlix.evaluation.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class TaskUpdateRequest extends TaskRequest {

    private Long id;
}
