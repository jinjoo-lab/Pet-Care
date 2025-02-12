package com.example.petcare.module.member.controller;

import com.example.petcare.module.member.dto.response.MemberResponse;
import com.example.petcare.module.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable("id") Long id) {
        // TODO
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> withdrawMemberById(@PathVariable("id") Long id) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<MemberResponse>> gatAllMembers() {
        // TODO
    }
}
