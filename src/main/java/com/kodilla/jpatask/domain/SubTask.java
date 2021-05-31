package com.kodilla.jpatask.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Status status;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @OneToMany(targetEntity = SubTask.class, fetch=FetchType.EAGER)
    private Set<Person> responsible = new HashSet<>();

    public SubTask() {

    }

    public SubTask(Long id, String name, Status status, Task task) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.task = task;
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

    public Task getTask() {
        return task;
    }

    public Set<Person> getResponsible() {
        return responsible;
    }
}
