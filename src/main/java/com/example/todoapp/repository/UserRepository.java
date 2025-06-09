package com.example.todoapp.repository;

import com.example.todoapp.entity.User;
import com.example.todoapp.entity.Otp;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
