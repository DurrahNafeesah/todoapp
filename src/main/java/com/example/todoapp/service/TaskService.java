package com.example.todoapp.service;

import com.example.todoapp.entity.Task;
import com.example.todoapp.entity.TaskStatusMaster;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.TaskRepository;
import com.example.todoapp.repository.TaskStatusRepository;
import com.example.todoapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskStatusRepository statusRepository;
    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TaskStatusRepository statusRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
    }

    public Task createTask(Task task) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        if (task.getStatus() != null && "COMPLETED".equalsIgnoreCase(task.getStatus().getName())) {
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
            if (task.getStatus() != null && "COMPLETED".equalsIgnoreCase(task.getStatus().getName())) {
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

            if (newTask.getStatus() != null && "COMPLETED".equalsIgnoreCase(newTask.getStatus().getName())) {
                task.setCompleted(true);
            } else {
                task.setCompleted(false);
            }

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

    public Task updateStatus(Long id, Long statusId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        TaskStatusMaster status = statusRepository.findById(statusId).orElseThrow(() -> new RuntimeException("Status not found"));

        return taskRepository.findByIdAndUser(id, user).map(task -> {
            task.setStatus(status);
            task.setCompleted("COMPLETED".equalsIgnoreCase(status.getName()));
            log.info("Updated status of task ID {} to {}", id, status.getName());
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public List<Task> findTaskBySorting(String field) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return taskRepository.findByUser(user, Sort.by(Sort.Direction.ASC, field));
    }

    public Page<Task> findTaskByPagination(int offset, int pageSize) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(offset, pageSize);
        return taskRepository.findByUser(user, pageable);
    }

    public Page<Task> searchTasksWithPaginationAndSorting(String keyword, int offset, int pageSize, String sortField) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(offset, pageSize, Sort.by(sortField));
        return taskRepository.findByUserAndTitleContainingIgnoreCase(user, keyword, pageable);
    }
}
