package ru.irlix.evaluation.dao.mapper.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.repository.RoleRepository;

@Component
@RequiredArgsConstructor
public class RoleHelper {

    private final RoleRepository roleRepository;

    public Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role with id " + id + " not found"));
    }
}
