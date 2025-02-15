package com.example.petcare.module.pet.dto.response;

import com.example.petcare.module.pet.entity.PetSize;
import com.example.petcare.module.pet.entity.PetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PetResponse {
    private Long id;
    private String name;
    private PetType petType;
    private Integer age;
    private String photo;
    private PetSize petSize;
}
