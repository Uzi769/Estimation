package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    private Integer repeatCount;

    private Integer bagsReserve;

    private Integer qaReserve;

    private Integer managementReserve;

    private String comment;

    private Integer hoursMin;

    private Integer hoursMax;

    @ManyToOne
    @JoinColumn(name = "phase")
    private Phase phase;

    @ManyToOne
    @JoinColumn(name = "estimation_role")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    private Task parent;

    @OneToMany(mappedBy = "parent")
    private List<Task> taskList;

    private boolean bagsReverseOn;

    private boolean qaReverseOn;

    private boolean managementReverseOn;

}
