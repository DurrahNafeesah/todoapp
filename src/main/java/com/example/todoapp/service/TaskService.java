package com.example.todoapp.service;

import com.example.todoapp.entity.Task;
import com.example.todoapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;
    
  

    public List<Task> saveAllTasks(List<Task> tasks) {
        return repository.saveAll(tasks);
    }

    public Task addTask(Task task) {
        return repository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        Task existing = repository.findById(id).orElse(null);
        if (existing != null) {
            existing.setDescription(task.getDescription());
            existing.setDueDate(task.getDueDate());
            existing.setStatus(task.getStatus());
            return repository.save(existing);
        }
        return null;
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Task getTaskById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteTask(Long id) {
        repository.deleteById(id);
    }
}
