package com.example.petcare.module.code.controller;

import com.example.petcare.module.code.dto.request.SaveCodeDetailRequest;
import com.example.petcare.module.code.dto.request.SaveCodeGroupRequest;
import com.example.petcare.module.code.dto.request.UpdateCodeDetailRequest;
import com.example.petcare.module.code.dto.request.UpdateCodeGroupRequest;
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
    public ResponseEntity<CodeGroupResponse> saveCodeGroup(@RequestBody SaveCodeGroupRequest request) {
        return ResponseEntity.ok(codeService.createCodeGroup(request));
    }

    @GetMapping("/groups")
    public ResponseEntity<List<CodeGroupResponse>> getCodeGroups() {
        return ResponseEntity.ok(codeService.getCodeGroups());
    }

    @GetMapping("/groups/{group-id}")
    public ResponseEntity<CodeGroupResponse> getCodeGroupById(@PathVariable("group-id") String groupId) {
        return ResponseEntity.ok(codeService.getCodeGroupById(groupId));
    }

    @PutMapping("/groups")
    public ResponseEntity<CodeGroupResponse> updateCodeGroup(@RequestBody UpdateCodeGroupRequest request) {
        return ResponseEntity.ok(codeService.updateCodeGroup(request));
    }

    @DeleteMapping("/groups/{group-id}")
    public ResponseEntity<List<CodeGroupResponse>> deleteCodeGroup(@PathVariable("group-id") String groupId) {
        codeService.deleteCodeGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/details")
    public ResponseEntity<CodeDetailResponse> saveCodeDetail(@RequestBody SaveCodeDetailRequest request) {
        return ResponseEntity.ok(codeService.createCodeDetail(request));
    }

    @GetMapping("/details")
    public ResponseEntity<List<CodeDetailResponse>> getCodeDetails() {
        return ResponseEntity.ok(codeService.getCodeDetails());
    }

    @GetMapping("/details/list/{group-id}")
    public ResponseEntity<List<CodeDetailResponse>> getCodeDetailsByGroup(@PathVariable("group-id") String groupId) {
        return ResponseEntity.ok(codeService.getCodeDetailsByGroup(groupId));
    }

    @GetMapping("/details/{detail-id}")
    public ResponseEntity<CodeDetailResponse> getCodeDetailById(@PathVariable("detail-id") String detailId) {
        return ResponseEntity.ok(codeService.getCodeDetailsById(detailId));
    }

    @PutMapping("/details")
    public ResponseEntity<CodeDetailResponse> updateCodeDetail(@RequestBody UpdateCodeDetailRequest request) {
        return ResponseEntity.ok(codeService.updateCodeDetail(request));
    }

    @DeleteMapping("/details/{detail-id}")
    public ResponseEntity<Void> deleteCodeDetail(@PathVariable("detail-id") String detailId) {
        codeService.deleteCodeDetail(detailId);
        return ResponseEntity.noContent().build();
    }
}
