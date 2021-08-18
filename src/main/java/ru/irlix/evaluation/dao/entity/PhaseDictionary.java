package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "phase_dictionary")
@Getter
@Setter
@NoArgsConstructor
public class PhaseDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer standardHours;

    @ManyToMany(mappedBy = "phaseDictionarySet")
    private Set<TaskDictionary> taskDictionarySet;
}
