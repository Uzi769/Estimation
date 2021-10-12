package ru.irlix.evaluation.dao.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.irlix.evaluation.dao.entity.OptionalRole;
import ru.irlix.evaluation.exception.NotFoundException;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.repository.role.OptionalRoleRepository;
import ru.irlix.evaluation.repository.role.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class RoleHelper {

    private final RoleRepository roleRepository;
    private final OptionalRoleRepository optionalRoleRepository;

    public Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role with id " + id + " not found"));
    }

    public List<Role> findOptionalRoles() {
        return optionalRoleRepository.findAll().stream()
                .map(OptionalRole::getRole)
                .collect(Collectors.toList());
    }
}
