package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "phase")
@Getter
@Setter
@NamedEntityGraph(
        name = "phase.tasks",
        attributeNodes = @NamedAttributeNode("tasks")
)
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimation")
    private Estimation estimation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL)
    @Where(clause = "parent_id IS NULL")
    @OrderBy("id ASC")
    private List<Task> tasks;

    @Column(name = "done")
    private Boolean done;

    @Column(name = "bags_reserve_on")
    private Boolean bagsReserveOn;

    @Column(name = "qa_reserve_on")
    private Boolean qaReserveOn;

    @Column(name = "management_reserve_on")
    private Boolean managementReserveOn;

    @Column(name = "risk_reserve_on")
    private Boolean riskReserveOn;
}
