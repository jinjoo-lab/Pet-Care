package com.example.petcare.module.pet.controller;

import com.example.petcare.module.pet.dto.request.SavePetRequest;
import com.example.petcare.module.pet.dto.response.PetResponse;
import com.example.petcare.module.pet.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<PetResponse> savePet(@RequestBody SavePetRequest request) {
        return ResponseEntity.ok(petService.saveRet(request));
    }

    @GetMapping("/{member-id}")
    public ResponseEntity<List<PetResponse>> getPetsByMember(@PathVariable("member-id") Long memberId) {
        return ResponseEntity.ok(petService.getPetsByMember(memberId));
    }
}
