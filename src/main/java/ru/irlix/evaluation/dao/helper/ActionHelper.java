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
        return findAction(value);
    }

    public Action findDeleteAction() {
        String value = "delete";
        return findAction(value);
    }

    public Action findRenameAction() {
        String value = "rename";
        return findAction(value);
    }

    public Action findEditUsersAction() {
        String value = "editUsers";
        return findAction(value);
    }

    public Action findEditFilesAction() {
        String value = "editFiles";
        return findAction(value);
    }

    public Action findChangeStatusAction() {
        String value = "changeStatus";
        return findAction(value);
    }

    public Action findLoadReportAction() {
        String value = "loadReport";
        return findAction(value);
    }

    private Action findAction(String value) {
        return repository.findByValue(value)
                .orElseThrow(() -> new NotFoundException("Action with value " + value + " not found"));
    }
}
