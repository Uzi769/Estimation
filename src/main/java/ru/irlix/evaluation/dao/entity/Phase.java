package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="phase")
@Getter
@Setter
@NoArgsConstructor
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "estimation")
    private Estimation estimation;

    private Integer sortOrder;

    private Integer managementReserve;

    private Integer bagsReserve;

    private Integer qaReserve;

    private Integer riskReserve;

    @OneToMany(mappedBy = "phase", fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    private List<Task> tasks;

    private Boolean done;

    private Boolean bagsReserveOn;

    private Boolean qaReserveOn;

    private Boolean managementReserveOn;

    private Boolean riskReserveOn;

}
