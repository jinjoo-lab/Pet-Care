package com.example.petcare.module.code.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CodeDetailResponse {
    private Long id;
    private String name;
    private Boolean isActive;
}
