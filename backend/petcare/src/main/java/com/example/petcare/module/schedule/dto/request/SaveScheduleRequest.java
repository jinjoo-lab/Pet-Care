package com.example.petcare.module.schedule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveScheduleRequest {
    private Long petSitterId;
    private String date;
    private Integer startTime;
    private Integer endTime;
    private Integer timeFee;
}
