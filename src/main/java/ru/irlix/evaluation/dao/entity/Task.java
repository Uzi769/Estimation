package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="task")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "risk_reserve")
    private Integer riskReserve;

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

    private Integer parent;
}
