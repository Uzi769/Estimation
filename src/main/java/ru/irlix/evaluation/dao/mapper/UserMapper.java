package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dto.UserKeycloakDto;
import ru.irlix.evaluation.dto.response.UserResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract UserResponse userToUserResponse(User user);

    public abstract List<UserResponse> usersToUserResponseList(List<User> users);

    @Mapping(target = "keycloakId", source = "id")
    public abstract User userKeycloakDtoToUser(UserKeycloakDto userKeycloakDto);
}
