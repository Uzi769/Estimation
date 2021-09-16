package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="status_dictionary")
@Getter
@Setter
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "display_value")
    private String displayValue;

    @OneToMany(mappedBy = "status")
    private List<Estimation> estimations;
}
