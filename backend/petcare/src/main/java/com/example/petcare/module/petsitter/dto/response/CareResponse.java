package com.example.petcare.module.petsitter.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CareResponse {
    private Long serviceId;
    private String name;
}
