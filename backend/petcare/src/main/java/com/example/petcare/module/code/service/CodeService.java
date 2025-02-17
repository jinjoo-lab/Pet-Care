package com.example.petcare.module.code.service;

import com.example.petcare.module.code.dto.request.SaveCodeDetailRequest;
import com.example.petcare.module.code.dto.request.SaveCodeGroupRequest;
import com.example.petcare.module.code.dto.response.CodeDetailResponse;
import com.example.petcare.module.code.dto.response.CodeGroupResponse;
import com.example.petcare.module.code.entity.CodeDetail;
import com.example.petcare.module.code.entity.CodeGroup;
import com.example.petcare.module.code.mapper.CodeMapper;
import com.example.petcare.module.code.repository.CodeDetailRepository;
import com.example.petcare.module.code.repository.CodeGroupRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CodeService {
    private CodeGroupRepository codeGroupRepository;
    private CodeDetailRepository codeDetailRepository;
    private CodeMapper codeMapper;

    public CodeService(
            CodeGroupRepository codeGroupRepository, CodeDetailRepository codeDetailRepository,
            CodeMapper codeMapper) {
        this.codeGroupRepository = codeGroupRepository;
        this.codeDetailRepository = codeDetailRepository;
        this.codeMapper = codeMapper;
    }

    public CodeGroupResponse createCodeGroup(SaveCodeGroupRequest request) {
        CodeGroup codeGroup = codeGroupRepository.save(
                codeMapper.saveCodeGroupRequesToCodeGroup(
                        request
                )
        );

        return codeMapper.codeGroupToResponse(codeGroup);
    }

    @Transactional
    public CodeDetailResponse createCodeDetail(SaveCodeDetailRequest request) {
        CodeGroup codeGroup = codeGroupRepository.findById(request.getCodeGroupId())
                .orElseThrow(EntityNotFoundException::new);

        CodeDetail codeDetail = codeDetailRepository.save(
                codeMapper.saveCodeDetailToCodeDetail(
                        request, codeGroup
                )
        );

        return codeMapper.codeDetailToResponse(codeDetail);
    }

    @Transactional(readOnly = true)
    public List<CodeGroupResponse> getCodeGroups() {
        return codeGroupRepository.findAll()
                .stream().map(c -> codeMapper.codeGroupToResponse(c))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CodeDetailResponse> getCodeDetails() {
        return codeDetailRepository.findAll()
                .stream().map(c -> codeMapper.codeDetailToResponse(c))
                .toList();
    }
}
