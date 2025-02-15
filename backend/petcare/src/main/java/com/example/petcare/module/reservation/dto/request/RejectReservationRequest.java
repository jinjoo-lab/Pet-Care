package com.example.petcare.module.reservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RejectReservationRequest {
    private Long reservationId;
    private Long scheduleId;
    private Long petSitterId;
}
