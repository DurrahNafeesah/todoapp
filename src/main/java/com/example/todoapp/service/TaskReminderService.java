package com.example.todoapp.service;

import com.example.todoapp.entity.Task;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskReminderService {

    private final TaskRepository taskRepository;
    private final EmailService emailService;

    public TaskReminderService(TaskRepository taskRepository, EmailService emailService) {
        this.taskRepository = taskRepository;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void sendTaskReminders() {
        LocalDateTime startOfTomorrow = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfTomorrow = startOfTomorrow.toLocalDate().atTime(23, 59, 59);

        List<Task> tasksDueTomorrow = taskRepository.findByDueDateBetweenAndCompletedFalse(startOfTomorrow, endOfTomorrow);
        for (Task task : tasksDueTomorrow) {
            User user = task.getUser();
            String to = user.getEmail();
            String subject = "⏰ Task Reminder: Due Tomorrow - " + task.getTitle();
            String message = "Hi " + user.getUsername() + ",\n\n" +
                    "You have a task due tomorrow:\n\n" +
                    "📌 Title: " + task.getTitle() + "\n" +
                    "🗓️ Due Date: " + task.getDueDate() + "\n\n" +
                    "Please make sure to complete it on time.\n\n- Todo App";

            emailService.sendSimpleMessage(to, subject, message);
        }
    }

}
