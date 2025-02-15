package com.example.petcare.module.reservation.service;

import com.example.petcare.module.member.entity.Member;
import com.example.petcare.module.member.service.MemberService;
import com.example.petcare.module.pet.entity.Pet;
import com.example.petcare.module.pet.repository.PetRepository;
import com.example.petcare.module.reservation.dto.request.SaveReservationRequest;
import com.example.petcare.module.reservation.dto.response.ReservationResponse;
import com.example.petcare.module.reservation.entity.PetReservation;
import com.example.petcare.module.reservation.entity.Reservation;
import com.example.petcare.module.reservation.mapper.ReservationMapper;
import com.example.petcare.module.reservation.repository.ReservationRepository;
import com.example.petcare.module.schedule.entity.Schedule;
import com.example.petcare.module.schedule.service.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;
    private final ScheduleService scheduleService;
    private final MemberService memberService;
    private final PetRepository petRepository;

    public ReservationService(
            ReservationRepository reservationRepository, ReservationMapper reservationMapper,
            ScheduleService scheduleService, MemberService memberService,
            PetRepository petRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.scheduleService = scheduleService;
        this.memberService = memberService;
        this.petRepository = petRepository;
    }

    @Transactional
    public ReservationResponse saveReservation(SaveReservationRequest request) {

        Member member = memberService.getMemberEntityById(request.getMemberId());
        Schedule schedule = scheduleService.getScheduleById(request.getScheduleId());
        List<Pet> pets = petRepository.findAllByIdIn(request.getPetList());

        Reservation reservation = reservationRepository.save(
                reservationMapper.saveReservationRequestToReservation(
                    member,
                    schedule,
                    request
            )
        );

        reservation.getPetReservations()
                .addAll(
                        pets.stream().map(
                                p -> new PetReservation(p, reservation)
                        ).toList()
                );

        return reservationMapper.reservationToReservationResponse(reservation);
    }

    @Transactional
    public ReservationResponse getReservationById(Long id) {
        return reservationMapper.reservationToReservationResponse(reservationRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }


}
