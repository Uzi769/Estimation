package ru.irlix.evaluation.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusRequest {
    private String value;
    private String displayValue;
}
