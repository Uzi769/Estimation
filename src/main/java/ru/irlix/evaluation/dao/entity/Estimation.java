package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;


import javax.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name="estimation")
@Getter
@Setter
@NoArgsConstructor
public class Estimation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @CreationTimestamp
    private Instant createDate;

    private String description;

    private Integer risk;

    @ManyToOne
    @JoinColumn(name = "status")
    private Status status;

    private String client;

    private String creator;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "estimation")
    private List<Phase> phases;
}
