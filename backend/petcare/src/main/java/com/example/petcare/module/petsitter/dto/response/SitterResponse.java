package com.example.petcare.module.petsitter.dto.response;

import com.example.petcare.module.member.dto.response.MemberResponse;
import com.example.petcare.module.petsitter.entity.Certified;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SitterResponse {
    private Long petSitterId;
    private MemberResponse memberResponse;
    private String location;
    private Certified certified;
    private Integer startTime;
    private Integer endTime;
    private Integer fee;
    private List<SpeciesResponse> species;
    private List<CareResponse> services;
}
