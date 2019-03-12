package com.example.resttesttask.repository;

import com.example.resttesttask.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    User findUserByUsername(String username);
}
