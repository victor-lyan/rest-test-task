package com.example.resttesttask.controller;

import com.example.resttesttask.dto.PetDto;
import com.example.resttesttask.exception.MyValidationException;
import com.example.resttesttask.exception.NotYourPetException;
import com.example.resttesttask.exception.PetNotFoundException;
import com.example.resttesttask.exception.UserNotFoundException;
import com.example.resttesttask.model.Pet;
import com.example.resttesttask.model.User;
import com.example.resttesttask.service.PetService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
public class PetController {

    private PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/users/{user}/pets")
    public List<PetDto> getPets(@PathVariable User user) throws UserNotFoundException {
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return petService.getUserPets(user);
    }

    @GetMapping("/pets/{pet}")
    public PetDto getPet(@PathVariable Pet pet) throws PetNotFoundException {
        if (pet == null) {
            throw new PetNotFoundException("Pet not found");
        }

        return new PetDto(pet);
    }

    @PostMapping("/users/{user}/pets")
    public PetDto addPet(@PathVariable User user, @Valid @RequestBody Pet pet, BindingResult result)
        throws UserNotFoundException, MyValidationException {
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        if (result.hasErrors()) {
            throw new MyValidationException(result.getAllErrors().stream()
                .map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
        }

        pet.setUser(user);
        petService.addPet(pet);

        return new PetDto(pet);
    }

    @PutMapping("/pets/{petId}")
    public PetDto updatePet(@PathVariable int petId, @RequestBody @Valid Pet pet, BindingResult result)
        throws PetNotFoundException, MyValidationException, NotYourPetException {
        Pet petInDb = petService.findById(petId);

        checkPet(petInDb);
        if (result.hasErrors()) {
            throw new MyValidationException(result.getAllErrors().stream()
                .map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
        }
        
        petService.updatePet(petInDb, pet);
        
        return new PetDto(petInDb);
    }

    @DeleteMapping("/pets/{pet}")
    public PetDto deletePet(@PathVariable Pet pet) throws PetNotFoundException, NotYourPetException {
        checkPet(pet);

        petService.deletePet(pet);
        
        return new PetDto(pet);
    }

    private void checkPet(Pet petInDb) throws PetNotFoundException, NotYourPetException {
        if (petInDb == null) {
            throw new PetNotFoundException("Pet not found");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        if (currentUser.getId() != petInDb.getUser().getId()) {
            throw new NotYourPetException("This is not your pet");
        }
    }
}
