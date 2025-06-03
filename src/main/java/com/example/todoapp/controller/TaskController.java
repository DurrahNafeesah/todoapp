package com.example.todoapp.controller;

import com.example.todoapp.dto.StatusUpdateRequest;
import com.example.todoapp.entity.Task;
import com.example.todoapp.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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

    @PostMapping("/bulk")
    public ResponseEntity<List<Task>> createBulkTasks(@Valid @RequestBody List<Task> tasks) {
        List<Task> savedTasks = taskService.createBulkTasks(tasks);
        return new ResponseEntity<>(savedTasks, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/sort/{field}")
    public ResponseEntity<List<Task>> getTaskBySorting(@PathVariable ("field") String field) {
        List<Task> tasks;
        tasks = taskService.findTaskBySorting(field);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/pagination/{offset}/{pageSize}")
    public ResponseEntity<Page<Task>> getTaskBySorting(@PathVariable int offset,@PathVariable int pageSize) {
        Page<Task> tasks;
        tasks = (Page<Task>) taskService.findTaskByPagination(offset,pageSize);
        return ResponseEntity.ok(tasks);
    }
//    @GetMapping("/pagination/{offset}/{pageSize}/{field}")
//    public ResponseEntity<List<Task>> getTaskBySorting(@PathVariable int offset,@PathVariable int pageSize,@PathVariable ("field") String field) {
//        List<Task> tasks;
//        tasks = taskService.findTaskByPaginationAndSorting(offset,pageSize,field);
//        return ResponseEntity.ok(tasks);
//    }
@GetMapping("/search")
public ResponseEntity<Page<Task>> searchTasksWithPaginationAndSorting(
        @RequestParam String keyword,
        @RequestParam int offset,
        @RequestParam int pageSize,
        @RequestParam ("field")String sortField) {

    Page<Task> tasks = taskService.searchTasksWithPaginationAndSorting(keyword, offset, pageSize, sortField);
    return ResponseEntity.ok(tasks);
}



    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest statusUpdateRequest) {
        Task task = taskService.updateStatus(id, statusUpdateRequest.getStatusId());
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }
}
