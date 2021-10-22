package ru.irlix.evaluation.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;


@Entity
@Table(name = "event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;

    @Column(name = "date")
    private Instant date;

    @Column(name = "estimation_id")
    private Long estimationId;

    @Column(name = "estimation_name")
    private String estimationName;

    @Column(name = "phase_id")
    private Long phaseId;

    @Column(name = "phase_name")
    private String phaseName;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "user_id")
    private Long userId;
}
