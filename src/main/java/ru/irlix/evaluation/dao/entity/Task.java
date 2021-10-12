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

    @Column(name = "bugs_reserve")
    private Integer bugsReserve;

    @Column(name = "qa_reserve")
    private Integer qaReserve;

    @Column(name = "pm_reserve")
    private Integer pmReserve;

    @Column(name = "comment")
    private String comment;

    @Column(name = "min_hours")
    private Double minHours;

    @Column(name = "max_hours")
    private Double maxHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase")
    private Phase phase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimation_role")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    private Task feature;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.REMOVE)
    @OrderBy("id ASC")
    private List<Task> tasks;

    @Column(name = "bugs_reserve_on")
    private Boolean bugsReserveOn;

    @Column(name = "qa_reserve_on")
    private Boolean qaReserveOn;

    @Column(name = "pm_reserve_on")
    private Boolean pmReserveOn;

    @PrePersist
    public void prePersist() {
        if (maxHours == null) {
            maxHours = 0.0;
        }

        if (minHours == null) {
            minHours = 0.0;
        }

        if (repeatCount == null) {
            repeatCount = 1;
        }

        if (bugsReserveOn == null) {
            bugsReserveOn = false;
        }

        if (qaReserveOn == null) {
            qaReserveOn = false;
        }

        if (pmReserveOn == null) {
            pmReserveOn = false;
        }

        if (bugsReserve == null) {
            bugsReserve = 0;
        }

        if (qaReserve == null) {
            qaReserve = 0;
        }

        if (pmReserve == null) {
            pmReserve = 0;
        }
    }
}
