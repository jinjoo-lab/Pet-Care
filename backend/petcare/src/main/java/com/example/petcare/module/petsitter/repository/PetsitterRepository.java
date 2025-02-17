package com.example.petcare.module.petsitter.repository;

import com.example.petcare.module.member.entity.Member;
import com.example.petcare.module.petsitter.entity.Petsitter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetsitterRepository extends JpaRepository<Petsitter, Long> {
    Optional<Petsitter> findByMember(Member member);
}
