package ru.irlix.evaluation.dao.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.Action;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.repository.ActionRepository;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class ActionHelper {

    private final ActionRepository repository;

    public Action findCreateAction() {
        String value = "create";
        return repository.findByValue(value)
                .orElseThrow(() -> getNotFoundException(value));
    }

    public Action findDeleteAction() {
        String value = "delete";
        return repository.findByValue(value)
                .orElseThrow(() -> getNotFoundException(value));
    }

    public Action findRenameAction() {
        String value = "rename";
        return repository.findByValue(value)
                .orElseThrow(() -> getNotFoundException(value));
    }

    public Action findEditUsersAction() {
        String value = "editUsers";
        return repository.findByValue(value)
                .orElseThrow(() -> getNotFoundException(value));
    }

    public Action findEditFilesAction() {
        String value = "editFiles";
        return repository.findByValue(value)
                .orElseThrow(() -> getNotFoundException(value));
    }

    public Action findChangeStatusAction() {
        String value = "changeStatus";
        return repository.findByValue(value)
                .orElseThrow(() -> getNotFoundException(value));
    }

    public Action findLoadReportAction() {
        String value = "loadReport";
        return repository.findByValue(value)
                .orElseThrow(() -> getNotFoundException(value));
    }

    private NotFoundException getNotFoundException(String value) {
        return new NotFoundException("Action with value " + value + " not found");
    }
}
