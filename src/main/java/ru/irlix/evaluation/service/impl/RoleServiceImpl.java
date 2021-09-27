package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.aspect.LogInfo;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dao.mapper.RoleMapper;
import ru.irlix.evaluation.dto.request.RoleRequest;
import ru.irlix.evaluation.dto.response.RoleResponse;
import ru.irlix.evaluation.repository.RoleRepository;
import ru.irlix.evaluation.service.RoleService;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper mapper;

    @LogInfo
    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = mapper.roleRequestToRole(roleRequest);
        Role savedRole = roleRepository.save(role);

        log.info("Role with id " + savedRole.getId() + " saved");
        return mapper.roleToRoleResponse(savedRole);
    }

    @LogInfo
    @Override
    @Transactional
    public RoleResponse updateRole(Long id, RoleRequest roleRequest) {
        Role role = findRoleById(id);
        checkAndUpdateFields(role, roleRequest);
        Role savedRole = roleRepository.save(role);

        log.info("Role with id " + savedRole.getId() + " updated");
        return mapper.roleToRoleResponse(savedRole);
    }

    @LogInfo
    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = findRoleById(id);
        roleRepository.delete(role);
        log.info("Role with id " + role.getId() + " deleted");
    }

    @LogInfo
    @Override
    @Transactional(readOnly = true)
    public RoleResponse findRoleResponseById(Long id) {
        Role role = findRoleById(id);
        log.info("Role with id " + role.getId() + " found");
        return mapper.roleToRoleResponse(role);
    }

    private Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role with id " + id + " not found"));
    }

    @LogInfo
    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> findAllRoles() {
        List<Role> roleList = roleRepository.findAll();
        log.info("All roles found");
        return mapper.rolesToRoleResponseList(roleList);
    }

    private void checkAndUpdateFields(Role role, RoleRequest roleRequest) {
        if (roleRequest.getValue() != null) {
            role.setValue(roleRequest.getValue());
        }
        if (roleRequest.getDisplayValue() != null) {
            role.setDisplayValue(roleRequest.getDisplayValue());
        }
    }
}
