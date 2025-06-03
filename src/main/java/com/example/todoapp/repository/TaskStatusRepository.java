package com.example.todoapp.repository;

import com.example.todoapp.entity.TaskStatusMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatusMaster, Long> {
    Optional<TaskStatusMaster> findByName(String name);
}
