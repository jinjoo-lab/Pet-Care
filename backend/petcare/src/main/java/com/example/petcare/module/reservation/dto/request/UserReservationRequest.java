package com.example.petcare.module.reservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserReservationRequest {
    private Long userId;
    private Long reservationId;
}
