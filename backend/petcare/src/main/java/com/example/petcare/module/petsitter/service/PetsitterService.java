package com.example.petcare.module.petsitter.service;

import com.example.petcare.module.member.entity.Member;
import com.example.petcare.module.member.service.MemberService;
import com.example.petcare.module.petsitter.dto.request.SitterSaveRequest;
import com.example.petcare.module.petsitter.dto.request.UpdateSitterRequest;
import com.example.petcare.module.petsitter.dto.response.SitterResponse;
import com.example.petcare.module.petsitter.entity.*;
import com.example.petcare.module.petsitter.mapper.PetsitterMapper;
import com.example.petcare.module.petsitter.repository.CareRepository;
import com.example.petcare.module.petsitter.repository.PetsitterRepository;
import com.example.petcare.module.petsitter.repository.SitterSpeciesRepository;
import com.example.petcare.module.petsitter.repository.SpeciesRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetsitterService {
    private final PetsitterRepository petsitterRepository;
    private final MemberService memberService;
    private final PetsitterMapper petsitterMapper;
    private final CareRepository careRepository;
    private final SpeciesRepository speciesRepository;
    private final SitterSpeciesRepository sitterSpeciesRepository;

    public PetsitterService(
            PetsitterRepository petsitterRepository, MemberService memberService,
            PetsitterMapper petsitterMapper, CareRepository careRepository,
            SpeciesRepository speciesRepository, SitterSpeciesRepository sitterSpeciesRepository) {
        this.petsitterRepository = petsitterRepository;
        this.memberService = memberService;
        this.petsitterMapper = petsitterMapper;
        this.careRepository = careRepository;
        this.speciesRepository = speciesRepository;
        this.sitterSpeciesRepository = sitterSpeciesRepository;
    }

    // 펫시터 등록
    @Transactional
    public SitterResponse registerPetsitter(SitterSaveRequest request) {

        if(isExistPetsitter(request.getMemberId())) {
            throw new EntityExistsException();
        }

        Member member = memberService.getMemberEntityById(request.getMemberId());

        Petsitter petsitter = petsitterMapper.saveRequestToPetsitter(request);
        petsitter.updateMember(member);

        petsitterRepository.save(petsitter);

        petsitter.getSitterCare().addAll(findAllCareByNames(petsitter, request.getServices()));
        petsitter.getSitterSpecies().addAll(findAllSpeciesByNames(petsitter, request.getPets()));

        return petsitterMapper.petSitterToResponse(petsitter);
    }

    @Transactional(readOnly = true)
    public List<SitterCare> findAllCareByNames (Petsitter sitter, List<String> services) {
            return careRepository.findAllByNameIn(services).stream()
                    .map(care -> new SitterCare(care, sitter))
                    .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SitterSpecies> findAllSpeciesByNames (Petsitter sitter, List<String> topSpecies) {
            return speciesRepository.findAllByNameIn(topSpecies).stream()
                    .map(species -> new SitterSpecies(sitter, species))
                    .collect(Collectors.toList());

    }

    @Transactional
    public void testInit() {

        if(!careRepository.existsByName("산책"))
            careRepository.save(new Care("산책"));

        if(!careRepository.existsByName("위탁돌봄"))
            careRepository.save(new Care("위탁돌봄"));

        if(!careRepository.existsByName("방문돌봄"))
            careRepository.save(new Care("방문돌봄"));

        if(!speciesRepository.existsByName("강아지"))
            speciesRepository.save(new Species("강아지"));

        if(!speciesRepository.existsByName("고양이"))
            speciesRepository.save(new Species("고양이"));

        if(!speciesRepository.existsByName("새"))
            speciesRepository.save(new Species("새"));

        if(!speciesRepository.existsByName("파충류"))
            speciesRepository.save(new Species("파충류"));

        if(!speciesRepository.existsByName("기타"))
            speciesRepository.save(new Species("기타"));
    }

    // 펫시터 검색 (상세 보기)
    public SitterResponse getPetsitterByMemberId(Long id) {
        return petsitterMapper.petSitterToResponse(
                petsitterRepository.findById(id).orElseThrow(EntityExistsException::new)
        );
    }

    @Transactional(readOnly = true)
    public SitterResponse getPetSitterByMember(Long id) {
        Member member = memberService.getMemberEntityById(id);

        return petsitterMapper.petSitterToResponse(
                petsitterRepository.findByMember(member).orElseThrow(EntityExistsException::new)
        );
    }

    private Boolean isExistPetsitter(Long id) {
        return memberService.getMemberEntityById(id).getPetsitter() != null;
    }

    // 펫시터 상세 보기

    @Transactional
    public SitterResponse updatePetSitter(UpdateSitterRequest request) {
        Petsitter petsitter = getPetSitterEntityById(request.getSitterId());

        List<SitterCare> cares = findAllCareByNames(petsitter, request.getServices());
        List<SitterSpecies> species = findAllSpeciesByNames(petsitter, request.getPets());

        petsitter.updatePetsitter(
                request.getLocation(),
                request.getStartTime(),
                request.getEndTime(),
                request.getFee()
        );

        return petsitterMapper.petSitterToResponse(petsitter);
    }


    //

    @Transactional(readOnly = true)
    public Petsitter getPetSitterEntityById(Long id) {
        return petsitterRepository.findById(id).orElseThrow(EntityExistsException::new);
    }

    @Transactional
    public void deletePetSitter(Long petsitterId) {
        Petsitter sitter = getPetSitterEntityById(petsitterId);
        sitter.getBaseEntity().setDeletedAt(LocalDateTime.now());
    }
}
