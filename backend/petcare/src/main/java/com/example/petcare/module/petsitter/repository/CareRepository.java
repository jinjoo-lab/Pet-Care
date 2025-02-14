package com.example.petcare.module.petsitter.repository;

import com.example.petcare.module.petsitter.entity.Care;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareRepository extends JpaRepository<Care, Long> {
    List<Care> findAllByNameIn(List<String> name);
}
