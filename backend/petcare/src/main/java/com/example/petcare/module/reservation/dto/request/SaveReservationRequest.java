package com.example.petcare.module.reservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveReservationRequest {
    private Long memberId;
    private Long scheduleId;
    private List<Long> petList;
    private String date;
    private Integer startTime;
    private Integer endTime;
    private String description;
}
