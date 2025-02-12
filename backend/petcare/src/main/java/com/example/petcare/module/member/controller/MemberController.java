package com.example.petcare.module.member.controller;

import com.example.petcare.module.member.service.MemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{id}")
    public void getMemberById(@PathVariable("id") Long id) {}

    @DeleteMapping("/{id}")
    public void withdrawMemberById(@PathVariable("id") Long id) {}

    @GetMapping("/list")
    public void gatAllMembers() {}
}
