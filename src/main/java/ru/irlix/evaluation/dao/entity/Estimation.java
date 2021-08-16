package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

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

    @Column(name = "create_date")
    @CreationTimestamp
    private Instant createDate;

    private String description;

    private Integer risk;

    @ManyToOne
    @JoinColumn(name = "status")
    private Status status;

    private String client;

    private String creator;

    @OneToMany(mappedBy = "estimation")
    private List<Phase> phases;
}
