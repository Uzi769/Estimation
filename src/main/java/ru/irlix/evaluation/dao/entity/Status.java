package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="status_dictionary")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATUSES_SEQ")
    @SequenceGenerator(name = "STATUSES_SEQ", sequenceName = "SEQUENCE_STATUS", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "display_value")
    private String displayValue;

    @OneToMany(mappedBy = "status")
    private List<Estimate> estimates;

}
