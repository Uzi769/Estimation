package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "phase_dictionary")
@Getter
@Setter
public class PhaseDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "standard_hours")
    private Integer standardHours;

    @ManyToMany(mappedBy = "phaseDictionaries")
    private Set<TaskDictionary> taskDictionaries;
}
