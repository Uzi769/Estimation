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
@NoArgsConstructor
public class TaskDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer hoursMin;

    private Integer hoursMax;

    @ManyToOne
    @JoinColumn(name = "type")
    private TaskTypeDictionary type;

    @ManyToMany
    @JoinTable(
            name = "task_phase",
            joinColumns = @JoinColumn(name = "task_dictionary_id"),
            inverseJoinColumns = @JoinColumn(name = "phase_dictionary_id"))
    private Set<PhaseDictionary> phaseDictionarySet;
}
