package com.example.petcare.module.schedule.mapper;

import com.example.petcare.module.member.mapper.MemberMapper;
import com.example.petcare.module.pet.mapper.PetMapper;
import com.example.petcare.module.petsitter.entity.Petsitter;
import com.example.petcare.module.petsitter.mapper.PetsitterMapper;
import com.example.petcare.module.reservation.dto.response.SimpleReservationResponse;
import com.example.petcare.module.reservation.entity.Reservation;
import com.example.petcare.module.schedule.dto.response.ScheduleResponse;
import com.example.petcare.module.schedule.dto.request.SaveScheduleRequest;
import com.example.petcare.module.schedule.entity.Schedule;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

    PetsitterMapper petsitterMapper;
    MemberMapper memberMapper;
    PetMapper petMapper;


    public ScheduleMapper(
            PetsitterMapper petsitterMapper, MemberMapper memberMapper,
            PetMapper petMapper) {
        this.petsitterMapper = petsitterMapper;
        this.memberMapper = memberMapper;
        this.petMapper = petMapper;
    }

    public Schedule saveScheduleRequestToEntity(Petsitter sitter, SaveScheduleRequest request) {
        return new Schedule(
                sitter,
                request.getDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getTimeFee()
        );
    }

    public ScheduleResponse scheduleToScheduleResponse(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                petsitterMapper.petSitterToResponse(schedule.getPetSitter()),
                schedule.getDate(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getTimeFee(),
                schedule.getReservations().stream().map(this::reservationToSimpleReservationResponse)
                        .toList()
        );
    }

    public SimpleReservationResponse reservationToSimpleReservationResponse(Reservation reservation) {
        return new SimpleReservationResponse(
                reservation.getId(),
                reservation.getStatus(),
                memberMapper.memberToMemberResponse(reservation.getMember()),
                reservation.getPetReservations().stream().map(
                        pet -> petMapper.petToPetResponse(pet.getPet())
                ).toList()
        );
    }
}
