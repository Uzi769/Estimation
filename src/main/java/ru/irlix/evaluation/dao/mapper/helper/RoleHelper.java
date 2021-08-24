package ru.irlix.evaluation.dao.mapper.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.irlix.evaluation.dao.entity.Role;
import ru.irlix.evaluation.repository.RoleRepository;

@Log4j2
@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class RoleHelper {

    private final RoleRepository roleRepository;

    public Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Method findRoleById: Role with id " + id + " not found");
                    return new NotFoundException("Role with id " + id + " not found");
                });
    }
}
