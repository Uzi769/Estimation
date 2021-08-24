package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="task")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "type")
    private TaskTypeDictionary type;

    @Column(name = "repeat_count")
    private Integer repeatCount;

    @Column(name = "bags_reserve")
    private Integer bagsReserve;

    @Column(name = "qa_reserve")
    private Integer qaReserve;

    @Column(name = "management_reserve")
    private Integer managementReserve;

    @Column(name = "comment")
    private String comment;

    @Column(name = "hours_min")
    private Integer hoursMin;

    @Column(name = "hours_max")
    private Integer hoursMax;

    @ManyToOne
    @JoinColumn(name = "phase")
    private Phase phase;

    @ManyToOne
    @JoinColumn(name = "estimation_role")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    private Task parent;

    @OneToMany(mappedBy = "parent")
    private Set<Task> tasks;

    @Column(name = "bags_reserve_on")
    private Boolean bagsReserveOn;

    @Column(name = "qa_reserve_on")
    private Boolean qaReserveOn;

    @Column(name = "management_reserve_on")
    private Boolean managementReserveOn;

}
