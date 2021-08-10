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
import javax.persistence.OneToMany;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name="estimate")
@Getter @Setter @NoArgsConstructor
public class Estimate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTIMATES_SEQ")
    @SequenceGenerator(name = "ESTIMATES_SEQ", sequenceName = "SEQUENCE_ESTIMATES", allocationSize=1)
    @Column(columnDefinition = "id")
    private Long id;
    @Column(columnDefinition = "name")
    private String name;
    @Column(columnDefinition = "create_date")
    private Instant createDate;
    @Column(columnDefinition = "description")
    private String description;
    @Column(columnDefinition = "risk")
    private Integer risk;
    @Column(columnDefinition = "status")
    private Integer status;
    @Column(columnDefinition = "client")
    private String client;
    @Column(columnDefinition = "creator")
    private String creator;

    @OneToMany(mappedBy = "estimate")
    private List<Phase> phase;

}
