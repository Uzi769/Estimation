package ru.irlix.evaluation.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
            name = "user_estimation",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "estimation_id"))
    private List<Estimation> estimations;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "keycloak_id")
    private UUID keycloakId;

    @PrePersist
    public void prePersist() {
        if (deleted == null) {
            deleted = false;
        }
    }
}