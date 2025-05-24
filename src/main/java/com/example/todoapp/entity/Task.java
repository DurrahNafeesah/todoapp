package com.example.todoapp.entity;

import com.example.todoapp.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private boolean completed;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be today or in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;

        // Automatically update 'completed' based on status
        if (status == TaskStatus.COMPLETED || status == TaskStatus.DONE) {
            this.completed = true;
        } else {
            this.completed = false;
        }
    }
}
