package com.example.todoapp.controller;

import com.example.todoapp.entity.Task;
import com.example.todoapp.service.TaskService;
import com.example.todoapp.dto.StatusUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        Task savedTask = taskService.createTask(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

//    @GetMapping
//    public List<Task> getAllTasks() {
//        return taskService.getAllTasks();
//    }
@GetMapping
public ResponseEntity<?> getAllTasks() {
    try {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    } catch (Exception e) {
        e.printStackTrace();  // Log full error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching tasks");
    }
}


    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task task) {
        return new ResponseEntity<>(taskService.updateTask(id, task), HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest statusUpdateRequest) {
        Task task = taskService.updateStatus(id, statusUpdateRequest.getStatus());
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }
}
