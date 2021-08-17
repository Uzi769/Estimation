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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "estimation")
    private Estimation estimation;

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
    @JoinColumn(name = "estimation_role")
    private Role role;

    @OneToMany(mappedBy = "phase", fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    private List<Task> tasks;

    private Boolean done;
}
