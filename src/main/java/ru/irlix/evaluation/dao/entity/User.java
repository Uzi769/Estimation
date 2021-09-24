package ru.irlix.evaluation.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "users")
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