package ru.irlix.evaluation.dao.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.irlix.evaluation.aspect.EventInfo;
import ru.irlix.evaluation.aspect.LogInfo;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.FileStorage;
import ru.irlix.evaluation.dao.entity.Folder;
import ru.irlix.evaluation.dao.mapper.FolderMapper;
import ru.irlix.evaluation.dto.response.FileResponse;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.exception.StorageException;
import ru.irlix.evaluation.repository.FileStorageRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Log4j2
@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)

public class FileStorageHelper {

    @Value("${file-path}")
    private String filePath;

    private final FileStorageRepository fileStorageRepository;
    private final FolderHelper folderHelper;
    private final FolderMapper folderMapper;

    @EventInfo
    @LogInfo
    public void storeFileList(List<MultipartFile> multipartFileList, Estimation estimation) {
        Path rootLocation = Paths.get(filePath);
        List<FileStorage> fileStorageList = new ArrayList<>();
        multipartFileList
                .forEach(file -> {
                    UUID uuid = UUID.randomUUID();
                    String fileName = file.getOriginalFilename();
                    String extension = Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
                    Path destinationFile = rootLocation.resolve(uuid + extension).normalize().toAbsolutePath();
                    try {
                        InputStream inputStream = file.getInputStream();
                        Files.copy(inputStream, destinationFile,
                                StandardCopyOption.REPLACE_EXISTING);
                        FileStorage fileStorage = FileStorage.builder()
                                .uuid(uuid)
                                .fileName(file.getOriginalFilename())
                                .docType(file.getContentType())
                                .folder(folderHelper.getFolder(file.getOriginalFilename()))
                                .estimation(estimation)
                                .build();
                        fileStorageList.add(fileStorage);
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error(e.getMessage());
                        throw new StorageException(e.getMessage());
                    }
                });
        fileStorageRepository.saveAll(fileStorageList);
    }

    @LogInfo
    public List<FileResponse> getFileResponseList(Estimation estimation) {
        List<FileResponse> fileResponseList = new ArrayList<>();
        List<Folder> folderList = folderHelper.findFolders();
        folderList.forEach(folder -> fileResponseList.add(getFileResponse(estimation, folder)));
        return fileResponseList;
    }

    private FileResponse getFileResponse(Estimation estimation, Folder folder) {
        FileResponse fileResponse = new FileResponse();
        fileResponse.setFolderResponse(folderMapper.folderToFolderResponse(folder));
        fileResponse.setFileMap(findFilesByEstimationAndFolder(estimation, folder));
        return fileResponse;
    }

    @LogInfo
    public void deleteFilesByEstimation(Estimation estimation) {
        List<FileStorage> fileStorageList = fileStorageRepository.findAllByEstimation(estimation);
        fileStorageList.forEach(this::deleteFile);
    }

    @LogInfo
    public void deleteFile(FileStorage fileStorage) {
        Path rootLocation = Paths.get(filePath);
        String fileName = fileStorage.getFileName();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        Path filePath = rootLocation.resolve(fileStorage.getUuid().toString() + extension).normalize();
        if (Files.exists(filePath))
            try {
                Files.delete(filePath);
                log.info("File with id " + fileStorage.getId() + " deleted");
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
    }

    private Map<Long, String> findFilesByEstimationAndFolder(Estimation estimation, Folder folder) {
        Map<Long, String> fileMap = new TreeMap<>();
        List<FileStorage> fileStorageList = fileStorageRepository.findAllByEstimationAndFolder(estimation, folder);
        fileStorageList.forEach(file -> {
            if (checkExist(file))
                fileMap.put(file.getId(), file.getFileName());
        });
        return fileMap;
    }

    private boolean checkExist(FileStorage fileStorage) {
        Path rootLocation = Paths.get(filePath);
        String fileName = fileStorage.getFileName();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        Path filePath = rootLocation.resolve(fileStorage.getUuid().toString() + extension).normalize();
        return Files.exists(filePath);
    }

    public FileStorage findFileById(Long id) {
        return fileStorageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("File with id " + id + " not found"));
    }
}
