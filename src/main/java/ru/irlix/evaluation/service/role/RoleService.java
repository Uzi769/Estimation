package ru.irlix.evaluation.service.role;

import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dto.request.RoleRequest;
import ru.irlix.evaluation.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);

    RoleResponse updateRole(Long id, RoleRequest roleRequest);

    void deleteRole(Long id);

    RoleResponse findRoleResponseById(Long id);

    List<RoleResponse> findAllRoles();
}
