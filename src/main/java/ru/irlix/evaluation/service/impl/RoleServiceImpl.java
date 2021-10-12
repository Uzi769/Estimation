package ru.irlix.evaluation.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.aspect.LogInfo;
import ru.irlix.evaluation.dao.entity.MainRole;
import ru.irlix.evaluation.dao.entity.OptionalRole;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.dao.mapper.RoleMapper;
import ru.irlix.evaluation.dto.request.RoleRequest;
import ru.irlix.evaluation.dto.response.RoleResponse;
import ru.irlix.evaluation.repository.role.MainRoleRepository;
import ru.irlix.evaluation.repository.role.OptionalRoleRepository;
import ru.irlix.evaluation.repository.role.RoleRepository;
import ru.irlix.evaluation.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MainRoleRepository mainRoleRepository;
    private final OptionalRoleRepository optionalRoleRepository;
    private final RoleMapper mapper;

    @LogInfo
    @Override
    @Transactional
    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = mapper.roleRequestToRole(roleRequest);
        Role savedRole = roleRepository.save(role);

        MainRole mainRole = new MainRole(role);
        mainRoleRepository.save(mainRole);

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
        List<MainRole> mainRoles = mainRoleRepository.findAll();
        List<Role> roles = mainRoles.stream()
                .map(MainRole::getRole)
                .collect(Collectors.toList());

        log.info("All roles found");
        return mapper.rolesToRoleResponseList(roles);
    }

    @LogInfo
    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> findAllOptionalRoles() {
        List<OptionalRole> optionalRoles = optionalRoleRepository.findAll();
        List<Role> roles = optionalRoles.stream()
                .map(OptionalRole::getRole)
                .collect(Collectors.toList());

        log.info("Optional roles found");
        return mapper.rolesToRoleResponseList(roles);
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
