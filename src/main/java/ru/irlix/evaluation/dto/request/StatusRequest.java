package ru.irlix.evaluation.dto.request;

import lombok.Data;

@Data
public class StatusRequest {
    private Long id;
    private String value;
    private String displayValue;
}
