package com.example.resttesttask.service;

import com.example.resttesttask.dto.PetDto;
import com.example.resttesttask.model.Pet;
import com.example.resttesttask.model.User;
import com.example.resttesttask.repository.PetRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {
    
    private PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<PetDto> getUserPets(User user) {
        List<PetDto> returnPets = new ArrayList<>();
        List<Pet> pets = petRepository.findByUserId(user.getId());
        
        for (Pet pet : pets) {
            returnPets.add(new PetDto(pet));
        }
        return returnPets;
    }

    public void addPet(Pet pet) {
        petRepository.save(pet);
    }

    public Pet findById(int petId) {
        return petRepository.findById(petId);
    }

    public void updatePet(Pet petInDb, Pet pet) {
        petInDb.setName(pet.getName());
        petInDb.setBirthDate(pet.getBirthDate());
        petInDb.setGender(pet.getGender());
        
        petRepository.save(petInDb);
    }

    public void deletePet(Pet pet) {
        petRepository.delete(pet);
    }
}
