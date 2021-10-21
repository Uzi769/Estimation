package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.irlix.evaluation.dao.entity.Folder;
import ru.irlix.evaluation.dto.response.FolderResponse;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class FolderMapper {

    public abstract FolderResponse folderToFolderResponse(Folder folder);

    @AfterMapping
    protected void map(@MappingTarget FolderResponse folderResponse, Folder folder) {
        folderResponse.setTypeDoc(getTypes(folder));
    }

    public Set<String> getTypes(Folder folder){
        return folder.getFileStorageList()
                .stream()
                .map(type -> type.getFileName()
                        .substring(type.getFileName().lastIndexOf("."))
                        .toLowerCase())
                .collect(Collectors.toSet());
    }
}
