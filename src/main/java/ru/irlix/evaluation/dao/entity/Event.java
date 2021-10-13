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

    @Column(name = "user_name")
    private String userName;

    @Column(name = "value")
    private String value;

    @Column(name = "date")
    private Instant date;

    @Column(name = "estimation_name")
    private String estimationName;

    @Column(name = "phase_name")
    private String phaseName;

    @Column(name = "task_name")
    private String taskName;


}
