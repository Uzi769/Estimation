package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.irlix.evaluation.dao.entity.Action;
import ru.irlix.evaluation.dao.mapper.ActionMapper;
import ru.irlix.evaluation.dto.response.ActionResponse;
import ru.irlix.evaluation.repository.ActionRepository;
import ru.irlix.evaluation.service.ActionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {

    private final ActionRepository repository;
    private final ActionMapper mapper;

    @Override
    public List<ActionResponse> findAllActions() {
        List<Action> actions = repository.findAll();
        return mapper.actionsToActionResponseList(actions);
    }
}
