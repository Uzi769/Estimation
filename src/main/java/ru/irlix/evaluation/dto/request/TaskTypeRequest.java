package ru.irlix.evaluation.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskTypeRequest {

    @NotNull(message = "{value.notNull}")
    private String value;
}
