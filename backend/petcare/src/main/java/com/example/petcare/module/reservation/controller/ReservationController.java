package com.example.petcare.module.reservation.controller;

import com.example.petcare.module.reservation.dto.request.RejectReservationRequest;
import com.example.petcare.module.reservation.dto.request.SaveReservationRequest;
import com.example.petcare.module.reservation.dto.request.UpdateReservationRequest;
import com.example.petcare.module.reservation.dto.request.UserReservationRequest;
import com.example.petcare.module.reservation.dto.response.ReservationResponse;
import com.example.petcare.module.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> saveReservation(@RequestBody SaveReservationRequest request) {
        return ResponseEntity.ok(reservationService.saveReservation(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(reservationService.findReservationById(id));
    }

    @PatchMapping("/approve")
    public ResponseEntity<ReservationResponse> approveReservation(@RequestBody UserReservationRequest request) {
        return ResponseEntity.ok(reservationService.approveReservation(request));
    }

    @PatchMapping("/cancel")
    public ResponseEntity<ReservationResponse> cancelReservation(@RequestBody UserReservationRequest request) {
        return ResponseEntity.ok(reservationService.cancelReservation(request));
    }

    @PatchMapping("/reject")
    public ResponseEntity<ReservationResponse> rejectReservation(@RequestBody RejectReservationRequest request) {
        return ResponseEntity.ok(reservationService.rejectReservation(request));
    }

    @PutMapping
    public ResponseEntity<ReservationResponse> updateReservation(@RequestBody UpdateReservationRequest request) {
        return ResponseEntity.ok(reservationService.updateReservation(request));
    }

}
