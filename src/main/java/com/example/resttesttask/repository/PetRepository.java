package com.example.resttesttask.repository;

import com.example.resttesttask.model.Pet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Integer> {
    
    List<Pet> findByUserId(int userId);
    
    Pet findById(int petId);
}
