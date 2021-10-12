package ru.irlix.evaluation.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FolderResponse {

    private Long id;

    private String value;

    private String displayValue;
}
