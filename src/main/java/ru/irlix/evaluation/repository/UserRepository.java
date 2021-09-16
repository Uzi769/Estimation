package ru.irlix.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.irlix.evaluation.dao.entity.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByKeycloakId(UUID id);
}
