package ru.irlix.evaluation.dto.response;

import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FolderResponse {

    private Long id;

    private String value;

    private String displayValue;

    private Set<String> typeDoc;
}
