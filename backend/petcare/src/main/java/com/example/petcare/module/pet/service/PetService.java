package com.example.petcare.module.pet.service;

import com.example.petcare.module.member.entity.Member;
import com.example.petcare.module.member.service.MemberService;
import com.example.petcare.module.pet.dto.request.SavePetRequest;
import com.example.petcare.module.pet.dto.response.PetResponse;
import com.example.petcare.module.pet.entity.Pet;
import com.example.petcare.module.pet.mapper.PetMapper;
import com.example.petcare.module.pet.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PetService {
    private PetRepository petRepository;
    private MemberService memberService;
    private PetMapper petMapper;

    public PetService(
            PetRepository petRepository, MemberService memberService,
            PetMapper petMapper
    ) {
        this.petRepository = petRepository;
        this.memberService = memberService;
        this.petMapper = petMapper;
    }

    @Transactional
    public PetResponse saveRet(SavePetRequest request) {
        Member member = memberService.getMemberEntityById(request.getMemberId());

        Pet pet = petRepository.save(
                petMapper.savePetRequestToPet(member,request)
        );

        return petMapper.petToPetResponse(pet);
    }

    @Transactional(readOnly = true)
    public List<PetResponse> getPetsByMember(Long memberId) {
        Member member = memberService.getMemberEntityById(memberId);

        return member.getPets().stream()
                .map(pet -> petMapper.petToPetResponse(pet))
                .toList();
    }
}
