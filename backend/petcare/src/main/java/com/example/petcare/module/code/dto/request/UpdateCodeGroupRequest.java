package com.example.petcare.module.code.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCodeGroupRequest {
    private String name;
    private Long id;
}
