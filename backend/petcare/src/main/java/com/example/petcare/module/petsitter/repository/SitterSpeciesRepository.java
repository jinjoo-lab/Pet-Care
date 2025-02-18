package com.example.petcare.module.petsitter.repository;

import com.example.petcare.module.petsitter.entity.Petsitter;
import com.example.petcare.module.petsitter.entity.SitterSpecies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SitterSpeciesRepository extends JpaRepository<SitterSpecies, Long> {

    List<SitterSpecies> findByPetsitter(Petsitter petsitter);
}
