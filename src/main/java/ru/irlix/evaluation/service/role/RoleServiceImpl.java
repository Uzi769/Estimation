package ru.irlix.evaluation.service.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dao.mapper.RoleMapper;
import ru.irlix.evaluation.dto.request.RoleRequest;
import ru.irlix.evaluation.dto.response.RoleResponse;
import ru.irlix.evaluation.repository.RoleRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper mapper;

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = mapper.roleRequestToRole(roleRequest);
        Role savedRole = roleRepository.save(role);
        return mapper.roleToRoleResponse(savedRole);
    }

    @Override
    public RoleResponse updateRole(Long id, RoleRequest roleRequest) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role with id" + id + "not found"));
        Role updatedRole = checkAndUpdateFields(role, roleRequest);
        Role savedRole = roleRepository.save(updatedRole);
        return mapper.roleToRoleResponse(savedRole);
    }

    @Override
    public void deleteRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role with id" + id + "not found"));
        roleRepository.delete(role);
    }

    @Override
    public RoleResponse findRoleResponseById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role with id" + id + "not found"));
        return mapper.roleToRoleResponse(role);
    }

    @Override
    public Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role with id" + id + "not found"));
    }

    @Override
    public List<RoleResponse> findAll() {
        List<Role> roleList = roleRepository.findAll();
        return mapper.rolesToRoleResponseList(roleList);
    }

    private Role checkAndUpdateFields(Role role, RoleRequest roleRequest) {
        if (roleRequest.getValue() != null) {
            role.setValue(roleRequest.getValue());
        }
        if (roleRequest.getDisplayValue() != null) {
            role.setDisplayValue(roleRequest.getDisplayValue());
        }
        return role;
    }
}
