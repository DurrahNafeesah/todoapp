package com.example.todoapp.service;

import com.example.todoapp.entity.Task;
import com.example.todoapp.entity.User;
import com.example.todoapp.enums.TaskStatus;
import com.example.todoapp.repository.TaskRepository;
import com.example.todoapp.repository.UserRepository;
import com.example.todoapp.security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(Task task) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        // Sync completed field with status
        if (task.getStatus() == TaskStatus.COMPLETED) {
            task.setCompleted(true);
        }

        task.setUser(user);
        log.info("Creating task: {}", task.getTitle());
        return taskRepository.save(task);
    }

    public List<Task> createBulkTasks(List<Task> tasks) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        for (Task task : tasks) {
            task.setUser(user);
            if (task.getStatus() == TaskStatus.COMPLETED) {
                task.setCompleted(true);
            }
        }

        log.info("Creating {} tasks", tasks.size());
        return taskRepository.saveAll(tasks);
    }

    public List<Task> getAllTasks() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByUser(user);
    }

    public Optional<Task> getTaskById(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByIdAndUser(id, user);
    }

    public Task updateTask(Long id, Task newTask) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByIdAndUser(id, user).map(task -> {
            task.setTitle(newTask.getTitle());
            task.setDescription(newTask.getDescription());
            task.setDueDate(newTask.getDueDate());
            task.setStatus(newTask.getStatus());

            // Automatically set completed if status is COMPLETED
            task.setCompleted(newTask.getStatus() == TaskStatus.COMPLETED || newTask.isCompleted());

            log.info("Updated task with ID {}", id);
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public void deleteTask(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskRepository.findByIdAndUser(id, user).orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }

    public Task updateStatus(Long id, TaskStatus status) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByIdAndUser(id, user).map(task -> {
            task.setStatus(status);
            task.setCompleted(status == TaskStatus.COMPLETED);
            log.info("Updated status of task ID {} to {}", id, status);
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }
}
