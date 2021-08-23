package ru.irlix.evaluation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import ru.irlix.evaluation.dao.entity.Phase;

import java.util.List;
import java.util.Optional;

public interface PhaseRepository extends JpaRepository<Phase, Long> {

    @Query("select distinct p from Phase p left join fetch p.tasks t where t.parent is null order by p.id")
    @NonNull
    List<Phase> findAll();

    @Query("select distinct p from Phase p left join fetch p.tasks t where t.parent is null and p.id = ?1")
    @NonNull
    Optional<Phase> findById(@NonNull Long id);
}
