package com.example.petcare.module.reservation.service;

import com.example.petcare.module.reservation.dto.request.SaveReservationRequest;
import com.example.petcare.module.reservation.dto.response.ReservationResponse;
import com.example.petcare.module.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationResponse saveReservation(SaveReservationRequest request) {

    }

    public ReservationResponse getReservationById(Long id) {

    }


}
