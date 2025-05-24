package com.example.todoapp.service;

import com.example.todoapp.entity.Task;
import com.example.todoapp.enums.TaskStatus;
import com.example.todoapp.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        log.info("Creating task: {}", task.getTitle());
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(Long id, Task newTask) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(newTask.getTitle());
            task.setDescription(newTask.getDescription());
            task.setDueDate(newTask.getDueDate());
            task.setStatus(newTask.getStatus());
            task.setCompleted(newTask.isCompleted());
            log.info("Updated task with ID {}", id);
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);
    }


    public Task updateStatus(Long id, TaskStatus status) {
        return taskRepository.findById(id).map(task -> {
            task.setStatus(TaskStatus.valueOf(String.valueOf(status)));
            log.info("Updated status of task ID {} to {}", id, status);
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }
}