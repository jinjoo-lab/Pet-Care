package com.example.petcare.module.schedule.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindScheduleRequest {
    private String location;
    private String date;
    private Integer startTime;
    private Integer endTime;
    private List<String> pets;
    private List<String> services;
}
