package com.example.petcare.module.code.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCodeDetailRequest {
    Boolean isActive;
    String name;
    Long codeDetailId;
}
