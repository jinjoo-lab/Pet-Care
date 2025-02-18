package com.example.petcare.module.reservation.service;

import com.example.petcare.module.member.entity.Member;
import com.example.petcare.module.member.service.MemberService;
import com.example.petcare.module.payment.entity.Payment;
import com.example.petcare.module.pet.entity.Pet;
import com.example.petcare.module.pet.repository.PetRepository;
import com.example.petcare.module.reservation.dto.request.*;
import com.example.petcare.module.reservation.dto.response.ReservationResponse;
import com.example.petcare.module.reservation.entity.PetReservation;
import com.example.petcare.module.reservation.entity.Reservation;
import com.example.petcare.module.reservation.entity.ReservationStatus;
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

    // 예약 등록, 수정, 승인, 취소

    @Transactional
    public ReservationResponse approveReservation(ApproveReservationRequest request) {
        Reservation reservation = getReservationById(request.getReservationId());

        System.out.println(reservation.getId()+" : "+reservation.getStatus());
        Schedule schedule = scheduleService.getScheduleById(request.getScheduleId());

        if (schedule.getPetSitter().getId().equals(request.getPetSitterId()) && reservation.getStatus().equals(ReservationStatus.REQUEST)) {
            reservation.updateStatus(ReservationStatus.APPROVE);
        }

        return reservationMapper.reservationToReservationResponse(reservation);
    }

    @Transactional
    public ReservationResponse cancelReservation(UserReservationRequest request) {
        Reservation reservation = getReservationById(request.getReservationId());

        if(reservation.getMember().getMemberId().equals(request.getUserId())) {
            reservation.updateStatus(ReservationStatus.CANCEL);
        }

        return reservationMapper.reservationToReservationResponse(reservation);
    }

    @Transactional
    public ReservationResponse rejectReservation(RejectReservationRequest request) {
        Reservation reservation = getReservationById(request.getReservationId());

        Schedule schedule = scheduleService.getScheduleById(request.getScheduleId());

        if (schedule.getPetSitter().getId().equals(request.getPetSitterId()) && reservation.getStatus().equals(ReservationStatus.REQUEST)) {
            reservation.updateStatus(ReservationStatus.REJECT);
        }

        return reservationMapper.reservationToReservationResponse(reservation);
    }

    @Transactional
    public ReservationResponse updateReservation(UpdateReservationRequest request) {
        Reservation reservation = getReservationById(request.getReservationId());

        reservation.updateReservation(request.getStartTime(),request.getEndTime(),request.getDescription());

        return reservationMapper.reservationToReservationResponse(reservation);
    }


    @Transactional(readOnly = true)
    public ReservationResponse findReservationById(Long id) {
        return reservationMapper.reservationToReservationResponse(reservationRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByMember(Long memberId) {

        Member member = memberService.getMemberEntityById(memberId);

        return reservationRepository.findAllByMember(member).stream().map(r -> reservationMapper.reservationToReservationResponse(r))
                .toList();
    }

    @Transactional
    public ReservationResponse paidReservation(UserReservationRequest request) {
        Reservation reservation = getReservationById(request.getReservationId());
        reservation.updateStatus(ReservationStatus.PAID);
        return reservationMapper.reservationToReservationResponse(reservation);
    }
}
