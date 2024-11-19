package com.ivory.ivory.controller;

import com.ivory.ivory.dto.ApplyDto;
import com.ivory.ivory.service.ApplyService;
import com.ivory.ivory.util.SecurityUtil;
import com.ivory.ivory.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {
    private final ApplyService applyService;
    private final SecurityUtil securityUtil;

    //서비스 신청
    @PostMapping()
    public ResponseEntity<?> applyService (@Valid @RequestBody ApplyDto dto) {
        Long currentMemberId = securityUtil.getCurrentMemberId();
        CustomApiResponse<?> response = applyService.applyService(dto,currentMemberId);
        return ResponseEntity.ok(response);
    }

    //신청 목록 조회
    @GetMapping()
    public ResponseEntity<?> getApplyList(@RequestParam Long childId) {
        Long currentMemberId = securityUtil.getCurrentMemberId();
        CustomApiResponse<?> response = applyService.getApplyList(childId,currentMemberId);
        return ResponseEntity.ok(response);
    }

    //신청 세부 조회
    @GetMapping("/{childId}")
    public ResponseEntity<?> getApplyDetail(@PathVariable Long childId) {
        Long currentMemberId = securityUtil.getCurrentMemberId();
        CustomApiResponse<?> response = applyService.getApplyDetail(childId,currentMemberId);
        return ResponseEntity.ok(response);
    }
}
