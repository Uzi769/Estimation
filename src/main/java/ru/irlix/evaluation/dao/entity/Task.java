package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "task")
@Getter
@Setter
@NamedEntityGraph(
        name = "task.tasks",
        attributeNodes = @NamedAttributeNode("tasks")
)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
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
    private Double hoursMin;

    @Column(name = "hours_max")
    private Double hoursMax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase")
    private Phase phase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimation_role")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    private Task parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    @OrderBy("id ASC")
    private List<Task> tasks;

    @Column(name = "bags_reserve_on")
    private Boolean bagsReserveOn;

    @Column(name = "qa_reserve_on")
    private Boolean qaReserveOn;

    @Column(name = "management_reserve_on")
    private Boolean managementReserveOn;

    @PrePersist
    public void prePersist() {
        if (hoursMax == null) {
            hoursMax = 0.0;
        }

        if (hoursMin == null) {
            hoursMin = 0.0;
        }

        if (repeatCount == null) {
            repeatCount = 1;
        }

        if (bagsReserveOn == null) {
            bagsReserveOn = false;
        }

        if (qaReserveOn == null) {
            qaReserveOn = false;
        }

        if (managementReserveOn == null) {
            managementReserveOn = false;
        }

        if (bagsReserve == null) {
            bagsReserve = 0;
        }

        if (qaReserve == null) {
            qaReserve = 0;
        }

        if (managementReserve == null) {
            managementReserve = 0;
        }
    }
}
