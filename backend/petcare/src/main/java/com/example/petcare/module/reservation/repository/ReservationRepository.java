package com.example.petcare.module.reservation.repository;

import com.example.petcare.module.member.entity.Member;
import com.example.petcare.module.petsitter.entity.Petsitter;
import com.example.petcare.module.reservation.entity.Reservation;
import com.example.petcare.module.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query(
            "SELECT r FROM Reservation r WHERE r.schedule = :schedule AND r.date = :date")
    List<Reservation> findByPetSitterAndDate(@Param("schedule") Schedule schedule, @Param("date") LocalDate date);

    List<Reservation> findAllByMember(Member member);
}
