package com.kodilla.jpatask.domain;

import javax.persistence.*;
import java.util.*;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Status status;

    @OneToMany(targetEntity = SubTask.class)
    private Set<Person> responsible = new HashSet<>();

    @OneToMany(targetEntity = SubTask.class, mappedBy = "task")
    private List<SubTask> subTasks = new ArrayList<>();;

    public Task() {

    }

    public Task(Long id, String name, Status status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public Set<Person> getResponsible() {
        return responsible;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }
}
