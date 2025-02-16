package com.example.petcare.module.pet.mapper;

import com.example.petcare.module.member.entity.Member;
import com.example.petcare.module.pet.dto.request.SavePetRequest;
import com.example.petcare.module.pet.dto.response.PetResponse;
import com.example.petcare.module.pet.entity.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    public Pet savePetRequestToPet(Member member, SavePetRequest request) {
        return new Pet(
                request.getName(),
                request.getPetType(),
                request.getAge(),
                request.getPhoto(),
                request.getSize(),
                member
        );
    }

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
