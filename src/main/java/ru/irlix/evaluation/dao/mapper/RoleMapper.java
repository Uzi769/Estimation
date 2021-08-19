package ru.irlix.evaluation.dao.mapper;

import org.mapstruct.Mapper;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dto.request.RoleRequest;
import ru.irlix.evaluation.dto.response.RoleResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role roleRequestToRole(RoleRequest roleRequest);

    RoleResponse roleToRoleResponse(Role role);

    List<RoleResponse> rolesToRoleResponseList(List<Role> roles);
}
