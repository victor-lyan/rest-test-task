package com.example.resttesttask.dto;

import com.example.resttesttask.model.Pet;

import java.time.LocalDate;

public class PetDto {
    
    private int id;
    private String name;
    private LocalDate birthDate;
    private String gender;
    private String owner;

    public PetDto(int id, String name, LocalDate birthDate, String gender, String owner) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.owner = owner;
    }
    
    public PetDto() {
        
    }
    
    public PetDto(Pet pet) {
        this.id = pet.getId();
        this.name = pet.getName();
        this.birthDate = pet.getBirthDate();
        this.gender = pet.getGender();
        this.owner = pet.getUser().getUsername();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
