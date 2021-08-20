package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name="estimation")
@Getter
@Setter
public class Estimation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @CreationTimestamp
    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "description")
    private String description;

    @Column(name = "risk")
    private Integer risk;

    @ManyToOne
    @JoinColumn(name = "status")
    private Status status;

    @Column(name = "client")
    private String client;

    @Column(name = "creator")
    private String creator;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "estimation")
    private Set<Phase> phases;
}
