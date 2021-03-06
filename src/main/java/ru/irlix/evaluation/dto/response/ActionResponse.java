package ru.irlix.evaluation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionResponse {

    private Long id;

    private String value;

    private String displayValue;
}
