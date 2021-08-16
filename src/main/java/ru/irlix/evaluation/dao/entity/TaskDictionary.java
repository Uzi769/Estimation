package ru.irlix.evaluation.dao.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "task_dictionary")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class TaskDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hours_min")
    private Integer hoursMin;

    @Column(name = "hours_max")
    private Integer hoursMax;
}
