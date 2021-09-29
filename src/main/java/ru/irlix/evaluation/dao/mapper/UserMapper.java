package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dto.UserKeycloakDto;
import ru.irlix.evaluation.dto.response.UserResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract UserResponse userToUserResponse(User user);

    public abstract List<UserResponse> usersToUserResponseList(List<User> users);

    @Mapping(target = "keycloakId", ignore = true)
    public abstract User userKeycloakDtoToUser(UserKeycloakDto userKeycloakDto);

    @AfterMapping
    protected void map(@MappingTarget User user, UserKeycloakDto userKeycloakDto) {
        if (userKeycloakDto != null) {
            user.setKeycloakId(userKeycloakDto.getId());
        }
    }
}
