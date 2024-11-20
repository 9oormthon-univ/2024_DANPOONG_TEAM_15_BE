package com.ivory.ivory.controller;

import com.ivory.ivory.dto.ApplyDto;
import com.ivory.ivory.dto.KakaoPayApproveDto;
import com.ivory.ivory.dto.KakaoPayReadyDto;
import com.ivory.ivory.service.ApplyService;
import com.ivory.ivory.service.KakaoPayService;
import com.ivory.ivory.util.SecurityUtil;
import com.ivory.ivory.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {
    private final ApplyService applyService;
    private final SecurityUtil securityUtil;
    private final KakaoPayService kakaoPayService;

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
    @GetMapping("/{applyId}")
    public ResponseEntity<?> getApplyDetail(@PathVariable Long applyId) {
        Long currentMemberId = securityUtil.getCurrentMemberId();
        CustomApiResponse<?> response = applyService.getApplyDetail(applyId,currentMemberId);
        return ResponseEntity.ok(response);
    }

    //카카오페이 결제 요청
    @PostMapping("/payments/{applyId}")
    public KakaoPayReadyDto kakaoPayReady(@PathVariable Long applyId) {
        return kakaoPayService.kakaoPayReady(applyId);
    }

    //카카오페에 결제 성공
    @PostMapping("/payments/success")
    public ResponseEntity<KakaoPayApproveDto> kakaoPaySuccess(@RequestParam("pg_token") String pgToken) {
        KakaoPayApproveDto kakaoPayApprove = kakaoPayService.approveResponse(pgToken);
        return ResponseEntity.ok(kakaoPayApprove);
    }

    //결제 진행 중 취소
    @PostMapping("/payments/cancel")
    public void cancel() {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"결제 진행 중 오류가 발생했습니다.");
    }

    //결제 실패
    @PostMapping("/payments/fail")
    public void fail() {
        throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"결제가 실패하였습니다.");
    }
}
