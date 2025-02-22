package com.example.petcare.module.reservation.controller;

import com.example.petcare.module.reservation.dto.request.*;
import com.example.petcare.module.reservation.dto.response.ReservationResponse;
import com.example.petcare.module.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ReservationResponse> approveReservation(@RequestBody ApproveReservationRequest request) {
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

    @PatchMapping("/paid")
    public ResponseEntity<ReservationResponse> paidReservation(@RequestBody UserReservationRequest request) {
        return ResponseEntity.ok(reservationService.paidReservation(request));
    }

    @PutMapping
    public ResponseEntity<ReservationResponse> updateReservation(@RequestBody UpdateReservationRequest request) {
        return ResponseEntity.ok(reservationService.updateReservation(request));
    }

    @GetMapping("/list/{member-id}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByMember(@PathVariable("member-id") Long memberId) {
        return ResponseEntity.ok(reservationService.getReservationsByMember(memberId));
    }
}
