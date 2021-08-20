package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.Mapper;
import ru.irlix.evaluation.dao.entity.Status;
import ru.irlix.evaluation.dto.request.StatusRequest;
import ru.irlix.evaluation.dto.response.StatusResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    Status statusRequestToStatus(StatusRequest statusRequest);

    StatusResponse statusToStatusResponse(Status status);

    List<StatusResponse> statusesToStatusResponseList(List<Status> statuses);
}
