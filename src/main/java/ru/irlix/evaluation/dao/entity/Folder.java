package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "folder_dictionary")
@Getter
@Setter
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private String value;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<FileStorage> fileStorageList;
}
