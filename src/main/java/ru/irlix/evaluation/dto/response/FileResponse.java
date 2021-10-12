package ru.irlix.evaluation.dto.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileResponse {

    private Long folderId;

    private String folderValue;

    private String folderDisplayValue;

    private Map<Long, String> fileMap;
}
