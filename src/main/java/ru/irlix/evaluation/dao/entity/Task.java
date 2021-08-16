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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    @SequenceGenerator(name = "task_seq", sequenceName = "task_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private Integer reapitCount;
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
    @JoinColumn(name = "phase", foreignKey = @ForeignKey(name="fk_phase_id"))
    private Phase phase;

    @ManyToOne
    @JoinColumn(name = "estimation_role", foreignKey = @ForeignKey(name="fk_estimation_role_id"))
    private Role role;

    @Column(name = "parent")
    private Integer parent;

}
