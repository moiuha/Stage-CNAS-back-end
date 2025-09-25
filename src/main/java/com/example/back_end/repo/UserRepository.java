package com.example.back_end.repo;

import com.example.back_end.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

}
