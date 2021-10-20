package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.Mapper;
import ru.irlix.evaluation.dao.entity.Action;
import ru.irlix.evaluation.dto.response.ActionResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActionMapper {

    ActionResponse actionToActionResponse(Action Action);

    List<ActionResponse> actionsToActionResponseList(List<Action> Actions);
}
