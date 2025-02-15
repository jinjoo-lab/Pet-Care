package com.example.petcare.module.reservation.mapper;

import com.example.petcare.module.member.entity.Member;
import com.example.petcare.module.member.mapper.MemberMapper;
import com.example.petcare.module.pet.mapper.PetMapper;
import com.example.petcare.module.reservation.dto.request.SaveReservationRequest;
import com.example.petcare.module.reservation.dto.response.ReservationResponse;
import com.example.petcare.module.reservation.entity.Reservation;
import com.example.petcare.module.reservation.entity.ReservationStatus;
import com.example.petcare.module.schedule.entity.Schedule;
import com.example.petcare.module.schedule.mapper.ScheduleMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ReservationMapper {

    MemberMapper memberMapper;
    ScheduleMapper scheduleMapper;
    PetMapper petMapper;

    public ReservationMapper(MemberMapper memberMapper, ScheduleMapper scheduleMapper,
                             PetMapper petMapper) {
        this.memberMapper = memberMapper;
        this.scheduleMapper = scheduleMapper;
        this.petMapper = petMapper;
    }

    public Reservation saveReservationRequestToReservation(
            Member member,
            Schedule schedule,
            SaveReservationRequest request
    ) {
        return new Reservation(
                schedule,
                member,
                convertDate(request.getDate()),
                request.getStartTime(),
                request.getEndTime(),
                ReservationStatus.REQUEST,
                request.getDescription()
        );
    }

    public ReservationResponse reservationToReservationResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getStatus(),
                scheduleMapper.scheduleToScheduleResponse(reservation.getSchedule()),
                memberMapper.memberToMemberResponse(reservation.getMember()),
                reservation.getPetReservations().stream().map(
                        pet -> petMapper.petToPetResponse(pet.getPet())
                ).toList()
        );
    }

    private LocalDate convertDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }
}
