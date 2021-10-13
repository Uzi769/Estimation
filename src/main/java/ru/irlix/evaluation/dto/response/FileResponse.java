package ru.irlix.evaluation.dto.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileResponse {

    private FolderResponse folderResponse;

    private Map<Long, String> fileMap;
}
