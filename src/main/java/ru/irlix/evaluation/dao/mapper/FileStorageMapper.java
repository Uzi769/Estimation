package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.Mapper;
import ru.irlix.evaluation.dao.entity.FileStorage;
import ru.irlix.evaluation.dto.response.FileStorageResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileStorageMapper {

    FileStorageResponse fileStorageToFileStorageResponse(FileStorage fileStorage);

    List<FileStorageResponse> fileStoragesToFileStorageList(List<FileStorage> fileStorageList);
}
