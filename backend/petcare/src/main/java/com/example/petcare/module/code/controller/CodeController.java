package com.example.petcare.module.code.controller;

import com.example.petcare.module.code.dto.request.SaveCodeDetailRequest;
import com.example.petcare.module.code.dto.request.SaveCodeGroupRequest;
import com.example.petcare.module.code.dto.response.CodeDetailResponse;
import com.example.petcare.module.code.dto.response.CodeGroupResponse;
import com.example.petcare.module.code.service.CodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/codes")
public class CodeController {
    private CodeService codeService;

    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    @PostMapping("/groups")
    public ResponseEntity<CodeGroupResponse> saveCodeGroup(SaveCodeGroupRequest request) {
        return ResponseEntity.ok(codeService.createCodeGroup(request));
    }

    @PostMapping("/details")
    public ResponseEntity<CodeDetailResponse> saveCodeDetail(SaveCodeDetailRequest request) {
        return ResponseEntity.ok(codeService.createCodeDetail(request));
    }

    @GetMapping("/groups")
    public ResponseEntity<List<CodeGroupResponse>> getCodeGroups() {
        return ResponseEntity.ok(codeService.getCodeGroups());
    }

    @GetMapping("/details")
    public ResponseEntity<List<CodeDetailResponse>> getCodeDetails() {
        return ResponseEntity.ok(codeService.getCodeDetails());
    }

    @GetMapping("/details/{group-id}")
    public ResponseEntity<List<CodeDetailResponse>> getCodeDetailsByGroup(@PathVariable("group-id") Long groupId) {
        return ResponseEntity.ok(codeService.getCodeDetailsByGroup(groupId));
    }
}
