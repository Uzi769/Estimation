package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "task_dictionary")
@Getter
@Setter
public class TaskDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "min_hours")
    private Integer minHours;

    @Column(name = "max_hours")
    private Integer maxHours;

    @ManyToOne
    @JoinColumn(name = "type")
    private TaskTypeDictionary type;

    @ManyToMany
    @JoinTable(
            name = "task_phase",
            joinColumns = @JoinColumn(name = "task_dictionary_id"),
            inverseJoinColumns = @JoinColumn(name = "phase_dictionary_id"))
    private Set<PhaseDictionary> phaseDictionaries;
}
