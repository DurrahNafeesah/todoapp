package com.example.todoapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "task_status_master")
public class TaskStatusMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // Constructors
    public TaskStatusMaster() {}

    public TaskStatusMaster(String name) {
        this.name = name;
    }

    public TaskStatusMaster(Object o, String aNew) {
    }

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
