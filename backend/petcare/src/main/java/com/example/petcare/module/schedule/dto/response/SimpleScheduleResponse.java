package com.example.petcare.module.schedule.dto.response;

import com.example.petcare.module.petsitter.dto.response.SitterResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleScheduleResponse {
    private Long id;
    private SitterResponse sitter;
    private LocalDate date;
    private Integer startTime;
    private Integer endTime;
    private Integer timeFee;
}
