package com.example.petcare.module.pet.dto.request;

import com.example.petcare.module.pet.entity.PetSize;
import com.example.petcare.module.pet.entity.PetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SavePetRequest {
    private String name;
    private PetType petType;
    private Integer age;
    private String photo;
    private PetSize size;
    private Long memberId;
}
