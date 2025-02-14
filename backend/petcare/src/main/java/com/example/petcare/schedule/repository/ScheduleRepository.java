package com.example.petcare.schedule.repository;

import com.example.petcare.module.petsitter.entity.Petsitter;
import com.example.petcare.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByPetSitterAndDate(Petsitter petsitter, LocalDate date);
}
