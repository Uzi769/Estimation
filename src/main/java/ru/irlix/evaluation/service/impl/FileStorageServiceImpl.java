package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.aspect.LogInfo;
import ru.irlix.evaluation.dao.entity.FileStorage;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dao.helper.UserHelper;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.FileStorageRepository;
import ru.irlix.evaluation.service.FileStorageService;
import ru.irlix.evaluation.utils.security.SecurityUtils;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Log4j2
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file-path}")
    private String filePath;

    private final FileStorageRepository fileStorageRepository;
    private final UserHelper userHelper;

    @LogInfo
    @Transactional(readOnly = true)
    @Override
    public Resource loadFileAsResource(Long id) {
        Path rootLocation = Paths.get(filePath);
        FileStorage fileStorage = findById(id);
        checkAccessToFile(fileStorage);
        String fileName = fileStorage.getFileName();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        Path filePath = rootLocation.resolve(fileStorage.getUuid().toString() + extension).normalize();
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists())
                return resource;
            else
                throw new NotFoundException("File not found " + fileStorage.getFileName());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new NotFoundException("File not found " + fileStorage.getFileName());
        }
    }

    private FileStorage findById(Long id) {
        return fileStorageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("File with id " + id + " not found"));
    }

    private void checkAccessToFile(FileStorage fileStorage) {
        User currentUser = userHelper.findUserByKeycloakId(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!SecurityUtils.hasAccessToAllEstimations() || !fileStorage.getEstimation().getUsers().contains(currentUser))
            throw new AccessDeniedException("User with id " + currentUser.getKeycloakId() + " cant get access to file");
    }
}
