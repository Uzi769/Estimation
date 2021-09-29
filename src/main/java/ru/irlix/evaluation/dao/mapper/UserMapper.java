package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.Mapper;
import ru.irlix.evaluation.dao.entity.User;
import ru.irlix.evaluation.dto.response.UserResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse userToUserResponse(User user);

    List<UserResponse> usersToUserResponseList(List<User> users);
}
