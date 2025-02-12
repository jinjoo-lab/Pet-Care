package com.example.petcare.module.member.service;

import com.example.petcare.module.member.dto.response.MemberResponse;
import com.example.petcare.module.member.entity.Member;
import com.example.petcare.module.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);

    }


}
