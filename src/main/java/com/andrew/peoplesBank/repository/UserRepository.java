package com.andrew.peoplesBank.repository;


import com.andrew.peoplesBank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User, Long> {

    Boolean existsByEmail(String email);

}
