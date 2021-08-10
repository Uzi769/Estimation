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
    @SequenceGenerator(name = "PHASES_SEQ", sequenceName = "SEQUENCE_PHASES", allocationSize = 1)
    @Column(columnDefinition = "id")
    private Long id;
    @Column(columnDefinition = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "estimate", foreignKey = @ForeignKey(name="FK_ESTIMATE"))
    private Estimate estimate;

    @Column(columnDefinition = "sort_order")
    private Integer sortOrder;
    @Column(columnDefinition = "management_reserve")
    private Integer managementReserve;
    @Column(columnDefinition = "bags_reserve")
    private Integer bagsReserve;
    @Column(columnDefinition = "qa_reserve")
    private Integer qaReserve;
    @Column(columnDefinition = "risk_reserve")
    private Integer riskReserve;
    @Column(columnDefinition = "estimate_role")
    private String estimateRole;

}
