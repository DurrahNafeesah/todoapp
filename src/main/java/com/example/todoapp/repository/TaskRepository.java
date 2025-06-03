package com.example.todoapp.repository;

import com.example.todoapp.entity.Task;
import com.example.todoapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findByUser(User user);

    List<Task> findByUser(User user,Sort sort);
    Optional<Task> findByIdAndUser(Long id, User user);

    List<Task> findByUser(User user, PageRequest pageRequest);
    Page<Task> findByUser(User user, Pageable pageable);

    // ✅ Add this method for searching by title + pagination + sorting
    Page<Task> findByUserAndTitleContainingIgnoreCase(User user, String keyword, Pageable pageable);
}
