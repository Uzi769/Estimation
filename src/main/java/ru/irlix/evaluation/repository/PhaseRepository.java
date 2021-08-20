package ru.irlix.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.irlix.evaluation.dao.entity.Phase;

import java.util.List;
import java.util.Optional;

public interface PhaseRepository extends JpaRepository<Phase, Long> {

    @Query("select p from Phase p join fetch p.tasks t where t.parent is null")
    @NonNull
    List<Phase> findAll();

    @Query("select p from Phase p join fetch p.tasks t where t.parent is null")
    @NonNull
    Optional<Phase> findById(@NonNull Long id);
}
