package ru.irlix.evaluation.dao.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "optional_role")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class OptionalRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @NonNull
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
