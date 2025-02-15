package com.example.petcare.module.reservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReservationRequest {
    private Long reservationId;
    private Integer startTime;
    private Integer endTime;
    private String description;
}
