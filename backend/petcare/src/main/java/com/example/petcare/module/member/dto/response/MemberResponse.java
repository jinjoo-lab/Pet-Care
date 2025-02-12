package com.example.petcare.module.member.dto.response;

import com.example.petcare.module.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
}
