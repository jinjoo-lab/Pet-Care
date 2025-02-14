package com.example.petcare.module.petsitter.controller;


import com.example.petcare.module.member.dto.request.SignUpRequest;
import com.example.petcare.module.member.service.MemberService;
import com.example.petcare.module.petsitter.dto.request.SitterSaveRequest;
import com.example.petcare.module.petsitter.dto.response.SitterResponse;
import com.example.petcare.module.petsitter.service.PetsitterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/petsitter")
public class PetsitterController {
    private final PetsitterService petsitterService;
    private final MemberService memberService;

    public PetsitterController(PetsitterService petsitterService,MemberService memberService) {
        this.petsitterService = petsitterService;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<SitterResponse> registerPetsitter(@RequestBody SitterSaveRequest sitterSaveRequest) {
        return ResponseEntity.ok(petsitterService.registerPetsitter(sitterSaveRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SitterResponse> getPetsitterByMemberId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(petsitterService.getPetsitterByMemberId(id));
    }

    @GetMapping("/test")
    public void test() {
        memberService.signUp(new SignUpRequest(
                "drasgon@naver.com",
                "1234",
                "jinjoowon",
                "01026485941"
        ));

        petsitterService.testInit();

        petsitterService.registerPetsitter(
                new SitterSaveRequest(
                        1L,
                        "서울시 강남구",
                        List.of("강아지", "고양이"),
                        List.of("산책", "위탁돌봄"),
                        8,
                        16,
                        15000
                )
        );
    }
}
