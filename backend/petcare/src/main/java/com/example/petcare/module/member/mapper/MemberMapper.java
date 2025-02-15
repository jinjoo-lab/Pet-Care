package com.example.petcare.module.member.mapper;

import com.example.petcare.module.member.dto.request.SignUpRequest;
import com.example.petcare.module.member.dto.response.MemberResponse;
import com.example.petcare.module.member.dto.response.ResetPasswordResponse;
import com.example.petcare.module.member.entity.Member;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@NoArgsConstructor
public class MemberMapper {


    public Member signUpRequestToMember(SignUpRequest signUpRequest) {
        return new Member(
                signUpRequest.getName(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getPhone()
        );
    }

    public MemberResponse memberToMemberResponse(Member member) {
        return new MemberResponse(
                member.getMemberId(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getRole()
        );
    }

    public ResetPasswordResponse memberToResetPasswordResponse(Member member) {
        return new ResetPasswordResponse(
                member.getEmail(),
                member.getPassword()
        );
    }

}
