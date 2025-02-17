package com.example.petcare.module.petsitter.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSitterRequest {
    private Long sitterId;
    private String location;
    private List<String> pets;
    private List<String> services;
    private Integer startTime;
    private Integer endTime;
    private Integer fee;
}
