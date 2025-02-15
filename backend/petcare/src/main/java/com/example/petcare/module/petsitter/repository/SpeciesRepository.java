package com.example.petcare.module.petsitter.repository;

import com.example.petcare.module.petsitter.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Long> {
    List<Species> findAllByTopSpeciesIn(List<String> topSpecies);
}
