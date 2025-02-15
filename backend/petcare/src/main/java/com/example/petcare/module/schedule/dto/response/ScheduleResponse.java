package com.example.petcare.module.schedule.dto.response;

import com.example.petcare.module.petsitter.dto.response.SitterResponse;
import com.example.petcare.module.reservation.dto.response.ReservationResponse;
import com.example.petcare.module.reservation.dto.response.SimpleReservationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {
    private Long id;
    private SitterResponse sitter;
    private LocalDate date;
    private Integer startTime;
    private Integer endTime;
    private Integer timeFee;
    private List<SimpleReservationResponse> reservations;
}
