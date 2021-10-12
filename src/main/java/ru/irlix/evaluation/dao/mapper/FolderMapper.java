package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.Mapper;
import ru.irlix.evaluation.dao.entity.Folder;
import ru.irlix.evaluation.dto.response.FolderResponse;

@Mapper(componentModel = "spring")
public interface FolderMapper {

    FolderResponse folderToFolderResponse(Folder folder);
}
