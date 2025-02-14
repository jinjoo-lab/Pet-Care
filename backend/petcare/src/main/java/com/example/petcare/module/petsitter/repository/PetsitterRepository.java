package com.example.petcare.module.petsitter.repository;

import com.example.petcare.module.petsitter.entity.Petsitter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetsitterRepository extends JpaRepository<Petsitter, Long> {
}
