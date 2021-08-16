package ru.irlix.evaluation.dao.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    @Column(name = "standard_hours")
    private Integer standardHours;
}
