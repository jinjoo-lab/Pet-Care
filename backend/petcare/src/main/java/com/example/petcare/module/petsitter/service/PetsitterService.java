package com.example.petcare.module.petsitter.service;

import com.example.petcare.module.petsitter.repository.PetsitterRepository;
import org.springframework.stereotype.Service;

@Service
public class PetsitterService {
    private final PetsitterRepository petsitterRepository;

    public PetsitterService(PetsitterRepository petsitterRepository) {
        this.petsitterRepository = petsitterRepository;
    }

    // 펫시터 등록
    public void registerPetsitter() {

    }

    // 펫시터 검색
    public void searchPetsitterByMember() {

    }

    // 펫시터 상세 보기

    //
}
