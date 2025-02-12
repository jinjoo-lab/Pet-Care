package com.example.petcare.module.member.controller;

import com.example.petcare.module.member.dto.request.ResetPasswordRequest;
import com.example.petcare.module.member.dto.request.SignUpRequest;
import com.example.petcare.module.member.dto.request.UpdateMemberRequest;
import com.example.petcare.module.member.dto.request.UpdatePasswordRequest;
import com.example.petcare.module.member.dto.response.MemberResponse;
import com.example.petcare.module.member.dto.response.ResetPasswordResponse;
import com.example.petcare.module.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 가입
    @PostMapping
    public ResponseEntity<MemberResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(memberService.signUp(request));
    }

    // 회원 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<MemberResponse> getMemberByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(memberService.getMemberByEmail(email));
    }

    // 비밀 번호 리셋
    @PatchMapping("/passwords/reset")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(memberService.resetPassword(request));
    }

    // 비밀번호 수정 , 회원 수정
    @PatchMapping("/passwords")
    public ResponseEntity<MemberResponse> updatePassword(@RequestBody UpdatePasswordRequest request) {
        return ResponseEntity.ok(memberService.updatePassword(request));
    }

    @PutMapping
    public ResponseEntity<MemberResponse> updateMember(@RequestBody UpdateMemberRequest request) {
        return ResponseEntity.ok(memberService.updateMember(request));
    }

    // 회원 탈퇴
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> withdrawMemberById(@PathVariable("id") Long id) {
        memberService.withdrawMember(id);
        return ResponseEntity.noContent().build();
    }
}
