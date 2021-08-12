package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.ForeignKey;

@Entity
@Table(name="phase")
@Getter
@Setter
@NoArgsConstructor
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHASES_SEQ")
    @SequenceGenerator(name = "PHASES_SEQ", sequenceName = "SEQUENCE_PHASE", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "estimate", foreignKey = @ForeignKey(name="FK_ESTIMATE"))
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
    @Column(name = "estimate_role")
    private String role;

}
