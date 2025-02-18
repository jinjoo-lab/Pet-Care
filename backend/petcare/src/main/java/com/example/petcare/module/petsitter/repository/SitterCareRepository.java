package com.example.petcare.module.petsitter.repository;

import com.example.petcare.module.petsitter.entity.Petsitter;
import com.example.petcare.module.petsitter.entity.SitterCare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SitterCareRepository extends JpaRepository<SitterCare, Long> {
    List<SitterCare> findByPetsitter(Petsitter petsitter);
}
