package ru.irlix.evaluation.dao.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.Folder;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.FolderRepository;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class FolderHelper {

    private final FolderRepository folderRepository;

    private static final List<String> docList;
    private static final List<String> picList;
    private static final List<String> audioList;
    private static final List<String> videoList;

    static {
        docList = List.of("word", "docx", "doc", "txt", "pdf", "xls", "xlsx", "html", "doc", "xml",
                "rtf", "ppt", "pptx", "epub", "odt", "log");
        picList = List.of("png", "jpeg", "jpg", "svg", "gif", "bmp");
        audioList = List.of("mid", "mp3", "wav", "aif", "ac3", "aac");
        videoList = List.of("avi", "mp4", "wmv", "mkv", "webm", "mov", "mpg", "mpeg");
    }

    public Folder findRoleById(Long id) {
        return folderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Folder with id " + id + " not found"));
    }

    public Folder getFolder(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (docList.contains(extension))
            return folderRepository.findByValue("documents");
        if (picList.contains(extension))
            return folderRepository.findByValue("pictures");
        if (audioList.contains(extension))
            return folderRepository.findByValue("audio");
        if (videoList.contains(extension))
            return folderRepository.findByValue("video");
        return folderRepository.findByValue("other");
    }

    public List<Folder> findFolders() {
        return folderRepository.findAll();
    }
}
