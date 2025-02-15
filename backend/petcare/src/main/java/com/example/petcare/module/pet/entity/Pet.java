package com.example.petcare.module.pet.entity;

import com.example.petcare.global.audit.AuditListener;
import com.example.petcare.module.petsitter.entity.Species;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@EntityListeners(AuditListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PetType petType;

    private Integer age;

    private String photo;

    @Enumerated(EnumType.STRING)
    private PetSize size;

    public Pet(String name, PetType petType, Integer age, String photo, PetSize size) {
        this.name = name;
        this.petType = petType;
        this.age = age;
        this.photo = photo;
        this.size = size;
    }
}
