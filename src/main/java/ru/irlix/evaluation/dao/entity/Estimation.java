package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name="estimation")
@Getter
@Setter
@NamedEntityGraph(
    name = "estimation.phases",
    attributeNodes = @NamedAttributeNode("phases")
)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    private Status status;

    @Column(name = "client")
    private String client;

    @Column(name = "creator")
    private String creator;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estimation")
    @OrderBy("id ASC")
    private List<Phase> phases;
}
