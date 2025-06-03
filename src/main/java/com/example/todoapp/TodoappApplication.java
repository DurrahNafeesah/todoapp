//package com.example.todoapp;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class TodoappApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(TodoappApplication.class, args);
//	}
//
//}
package com.example.todoapp;

import com.example.todoapp.entity.TaskStatusMaster;
import com.example.todoapp.repository.TaskStatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TodoappApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoappApplication.class, args);
	}

	@Bean
	public CommandLineRunner initStatuses(TaskStatusRepository statusRepository) {
		return args -> {
			if (statusRepository.count() == 0) {
				statusRepository.save(new TaskStatusMaster(null, "NEW"));
				statusRepository.save(new TaskStatusMaster(null, "STARTED"));
				statusRepository.save(new TaskStatusMaster(null, "COMPLETED"));
				System.out.println("Initialized task statuses.");
			}
		};
	}
}

