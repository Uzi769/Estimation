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

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "pm_reserve")
    private Integer pmReserve;

    @Column(name = "bugs_reserve")
    private Integer bugsReserve;

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

    @Column(name = "bugs_reserve_on")
    private Boolean bugsReserveOn;

    @Column(name = "qa_reserve_on")
    private Boolean qaReserveOn;

    @Column(name = "pm_reserve_on")
    private Boolean pmReserveOn;

    @Column(name = "risk_reserve_on")
    private Boolean riskReserveOn;

    @PrePersist
    public void prePersist() {
        if (riskReserve == null) {
            riskReserve = 0;
        }

        if (riskReserveOn == null) {
            riskReserveOn = false;
        }
    }
}
