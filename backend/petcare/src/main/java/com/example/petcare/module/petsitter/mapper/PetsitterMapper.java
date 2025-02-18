package com.example.petcare.module.petsitter.mapper;

import com.example.petcare.module.member.mapper.MemberMapper;
import com.example.petcare.module.petsitter.dto.request.SitterSaveRequest;
import com.example.petcare.module.petsitter.dto.response.CareResponse;
import com.example.petcare.module.petsitter.dto.response.SitterResponse;
import com.example.petcare.module.petsitter.dto.response.SpeciesResponse;
import com.example.petcare.module.petsitter.entity.Petsitter;
import com.example.petcare.module.petsitter.entity.Care;
import com.example.petcare.module.petsitter.entity.SitterCare;
import com.example.petcare.module.petsitter.entity.SitterSpecies;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PetsitterMapper {

    MemberMapper memberMapper;

    public PetsitterMapper(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public Petsitter saveRequestToPetsitter(SitterSaveRequest request) {
        return new Petsitter(
                request.getLocation(),
                request.getStartTime(),
                request.getEndTime(),
                request.getFee()
        );
    }

    public SitterCare toSitterService(Care service, Petsitter petsitter) {
        return new SitterCare(
                service, petsitter
        );
    }

    public List<CareResponse> toServiceResponse(List<SitterCare> cares) {
        return cares.stream()
                .map(x -> new CareResponse(
                        x.getService().getId(),
                        x.getService().getName()
                )).toList();
    }

    public List<SpeciesResponse> toSpeciesResponse(List<SitterSpecies> species) {
        return species.stream()
                .map(x -> new SpeciesResponse(
                        x.getSpecies().getId(),
                        x.getSpecies().getName()
                )).toList();
    }

    public SitterResponse petSitterToResponse(Petsitter petsitter) {
        return new SitterResponse(
                petsitter.getId(),
                memberMapper.memberToMemberResponse(petsitter.getMember()),
                petsitter.getLocation(),
                petsitter.getCertified(),
                petsitter.getStartTime(),
                petsitter.getEndTime(),
                petsitter.getFee(),
                toSpeciesResponse(petsitter.getSitterSpecies()),
                toServiceResponse(petsitter.getSitterCare())
        );
    }
}
