package com.example.petcare.module.code.dto.request;

import com.example.petcare.module.code.entity.CodeGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveCodeDetailRequest {
    Boolean isActive;
    String name;
    Long codeGroupId;
}
