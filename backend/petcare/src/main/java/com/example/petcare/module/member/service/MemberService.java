package com.example.petcare.module.member.service;

import com.example.petcare.module.member.dto.request.*;
import com.example.petcare.module.member.dto.response.DuplicateEmailResponse;
import com.example.petcare.module.member.dto.response.MemberResponse;
import com.example.petcare.module.member.dto.response.ResetPasswordResponse;
import com.example.petcare.module.member.entity.Member;
import com.example.petcare.module.member.mapper.MemberMapper;
import com.example.petcare.module.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    // 회원 가입 , 회원 조회, 비밀번호 리셋

    // 회원 수정, 회원 탈퇴


    public MemberResponse signUp(SignUpRequest request) {
        Member member = memberRepository.save(memberMapper.signUpRequestToMember(request));
        return memberMapper.memberToMemberResponse(member);
    }

    @Transactional
    public void signIn(SignInRequest request, HttpSession session) {
        Member member = memberRepository.findByEmailAndPassword(request.getEmail(),request.getPassword())
                .orElseThrow(EntityNotFoundException::new);

        session.setAttribute("user", member);
    }

    public DuplicateEmailResponse duplicateEmail(String email) {
        return new DuplicateEmailResponse(memberRepository.existsByEmail(email));
    }

    @Transactional
    public MemberResponse updateMember(UpdateMemberRequest request) {
        Member member = memberRepository.findById(request.getId()).orElseThrow(EntityNotFoundException::new);
        member.updateMember(request.getName(), request.getPhone());
        return memberMapper.memberToMemberResponse(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return memberMapper.memberToMemberResponse(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return memberMapper.memberToMemberResponse(member);
    }

    @Transactional
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(EntityNotFoundException::new);
        String tmpPassword = generateRandomString();
        member.updatePassword(tmpPassword);

        return memberMapper.memberToResetPasswordResponse(member);
    }

    @Transactional
    public MemberResponse updatePassword(UpdatePasswordRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(EntityNotFoundException::new);

        if(member.getPassword().equals(request.getOldPassword())){
            member.updatePassword(request.getNewPassword());
        }

        return memberMapper.memberToMemberResponse(member);
    }

    @Transactional
    public void withdrawMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        member.getBaseEntity().setDeletedAt(LocalDateTime.now());
    }

    private static String generateRandomString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
}
