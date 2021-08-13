package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.ForeignKey;
import javax.persistence.OneToMany;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name="estimate")
@Getter
@Setter
@NoArgsConstructor
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estimate_seq")
    @SequenceGenerator(name = "estimate_seq", sequenceName = "sequence_estimate", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "create_date")
    private Instant createDate;
    @Column(name = "description")
    private String description;
    @Column(name = "risk")
    private Integer risk;

    @ManyToOne
    @JoinColumn(name = "status", foreignKey = @ForeignKey(name="FK_STATUS"))
    private Status status;

    @Column(name = "client")
    private String client;
    @Column(name = "creator")
    private String creator;

    @OneToMany(mappedBy = "estimate")
    private List<Phase> phases;

}
