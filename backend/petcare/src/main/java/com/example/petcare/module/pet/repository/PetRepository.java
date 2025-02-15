package com.example.petcare.module.pet.repository;

import com.example.petcare.module.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findAllByIdIn(List<Long> petIds);
}
