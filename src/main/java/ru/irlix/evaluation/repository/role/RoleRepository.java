package ru.irlix.evaluation.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.evaluation.dao.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
