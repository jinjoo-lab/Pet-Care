package com.example.petcare.module.code.mapper;

import com.example.petcare.module.code.dto.request.SaveCodeDetailRequest;
import com.example.petcare.module.code.dto.request.SaveCodeGroupRequest;
import com.example.petcare.module.code.dto.response.CodeDetailResponse;
import com.example.petcare.module.code.dto.response.CodeGroupResponse;
import com.example.petcare.module.code.entity.CodeDetail;
import com.example.petcare.module.code.entity.CodeGroup;
import org.springframework.stereotype.Component;

@Component
public class CodeMapper {
    public CodeGroup saveCodeGroupRequestToCodeGroup(SaveCodeGroupRequest request) {
        return new CodeGroup(
                request.getId(),
                request.getName()
        );
    }

    public CodeDetail saveCodeDetailToCodeDetail(SaveCodeDetailRequest request, CodeGroup codeGroup) {
        return new CodeDetail(
                request.getDetailId(),
                request.getIsActive(),
                request.getName(),
                codeGroup
        );
    }

    public CodeGroupResponse codeGroupToResponse(CodeGroup codeGroup) {
        return new CodeGroupResponse(
                codeGroup.getId(),
                codeGroup.getName()
        );
    }

    public CodeDetailResponse codeDetailToResponse(CodeDetail codeDetail) {
        return new CodeDetailResponse(
                codeDetail.getId(),
                codeDetail.getName(),
                codeDetail.getIsActive()
        );
    }
}
