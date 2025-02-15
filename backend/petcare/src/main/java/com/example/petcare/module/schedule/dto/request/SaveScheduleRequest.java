package com.example.petcare.module.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveScheduleRequest {
    private Long petSitterId;
    private LocalDate date;
    private Integer startTime;
    private Integer endTime;
    private Integer timeFee;
}
