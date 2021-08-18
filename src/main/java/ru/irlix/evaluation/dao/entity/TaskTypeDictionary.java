package ru.irlix.evaluation.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "task_type_dictionary")
@Getter
@Setter
@NoArgsConstructor
public class TaskTypeDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @OneToMany(mappedBy = "type")
    private List<Task> taskList;

    @OneToMany(mappedBy = "type")
    private List<TaskDictionary> taskDictionaryList;
}
