package ru.irlix.evaluation.dao.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.irlix.evaluation.aspect.LogInfo;
import ru.irlix.evaluation.dao.entity.Estimation;
import ru.irlix.evaluation.dao.entity.FileStorage;
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
    public Map<Long, String> getFileMap(Estimation estimation) {
        Map<Long, String> fileMap = new HashMap<>();
        List<FileStorage> fileStorageList = fileStorageRepository.findAllByEstimation(estimation);
        fileStorageList.forEach(file -> {
            fileMap.put(file.getId(), file.getFileName());
        });
        return fileMap;
    }
}
