package com.example.petcare.module.pet.mapper;

import com.example.petcare.module.pet.dto.response.PetResponse;
import com.example.petcare.module.pet.entity.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {
    public PetResponse petToPetResponse(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getPetType(),
                pet.getAge(),
                pet.getPhoto(),
                pet.getSize()
        );
    }
}
