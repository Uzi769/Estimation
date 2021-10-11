package ru.irlix.evaluation.dao.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.Folder;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.FolderRepository;

import java.util.Arrays;

@Log4j2
@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class FolderHelper {

    private final FolderRepository folderRepository;

    private static final String[] DOCUMENTS_LIST = {
            "word", "docx", "doc", "txt", "pdf", "xls", "doc", "xml", "rtf", "zip", "7z", "rar"
    };

    private static final String[] PICTURES_LIST = {
            "png", "jpeg", "jpg", "svg", "gif", "bmp"
    };

    private static final String[] AUDIO_LIST = {
            "mid", "mp3", "wav"
    };

    public Folder findRoleById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Folder with id " + id + " not found"));
    }

    public Folder getFolder(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (Arrays.asList(DOCUMENTS_LIST).contains(extension))
            return folderRepository.findByValue("documents");
        if (Arrays.asList(PICTURES_LIST).contains(extension))
            return folderRepository.findByValue("pictures");
        if (Arrays.asList(AUDIO_LIST).contains(extension))
            return folderRepository.findByValue("audio");
        return folderRepository.findByValue("other");
    }
}
