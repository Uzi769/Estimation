package ru.irlix.evaluation.dao.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "main_role")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class MainRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @NonNull
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;
}
