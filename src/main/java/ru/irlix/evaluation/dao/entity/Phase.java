package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="phase")
@Getter
@Setter
@NoArgsConstructor
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phase_seq")
    @SequenceGenerator(name = "phase_seq", sequenceName = "phase_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "estimate", foreignKey = @ForeignKey(name="fk_estimate"))
    private Estimate estimate;

    @Column(name = "sort_order")
    private Integer sortOrder;
    @Column(name = "management_reserve")
    private Integer managementReserve;
    @Column(name = "bags_reserve")
    private Integer bagsReserve;
    @Column(name = "qa_reserve")
    private Integer qaReserve;
    @Column(name = "risk_reserve")
    private Integer riskReserve;

    @ManyToOne
    @JoinColumn(name = "estimate_role", foreignKey = @ForeignKey(name="fk_estimate_role"))
    private Role role;

    @OneToMany(mappedBy = "phase")
    private List<Task> tasks;

    @Column(name = "done")
    private Boolean done;

}
