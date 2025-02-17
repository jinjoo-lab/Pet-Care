package com.example.petcare.module.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScheduleRequest {
    private Long id;
    private Integer startTime;
    private Integer endTime;
}
