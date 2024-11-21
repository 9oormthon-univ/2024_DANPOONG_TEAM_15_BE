package com.ivory.ivory.controller;

import com.ivory.ivory.dto.AbsenceCertificatePageDto;
import com.ivory.ivory.dto.AbsenceCertificateRequestDto;
import com.ivory.ivory.dto.AbsenceCertificateResponseDto;
import com.ivory.ivory.dto.ChildRequestDto;
import com.ivory.ivory.dto.MedicalCertificatePageDto;
import com.ivory.ivory.dto.MedicalCertificateRequestDto;
import com.ivory.ivory.dto.MedicalCertificateResponseDto;
import com.ivory.ivory.service.AbsenceCertificateService;
import com.ivory.ivory.service.ChildService;
import com.ivory.ivory.service.MedicalCertificateService;
import com.ivory.ivory.util.SecurityUtil;
import com.ivory.ivory.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/children")
@RequiredArgsConstructor
public class ChildController {
    private final ChildService childService;
    private final MedicalCertificateService medicalCertificateService;
    private final AbsenceCertificateService absenceCertificateService;
    private final SecurityUtil securityUtil;

    //자녀 등록
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> addChild(@Valid @ModelAttribute ChildRequestDto dto) {
        Long currentUserId = securityUtil.getCurrentMemberId();
        CustomApiResponse<?> result = childService.addChild(dto,currentUserId);
        return ResponseEntity.ok(result);
    }

    //자녀 목록 조회
    @GetMapping()
    public ResponseEntity<?> getChildren() {
        Long currentUserId = securityUtil.getCurrentMemberId();
        CustomApiResponse<?> result = childService.getChildren(currentUserId);
        return ResponseEntity.ok(result);
    }

    //진단서 등록
    @PostMapping(consumes = {"multipart/form-data"}, value = "/{childId}/medical-certificates")
    public CustomApiResponse<?> addMedicalCertificate(
            @PathVariable Long childId,
            @Valid @ModelAttribute MedicalCertificateRequestDto requestDto) {
        Long currentUserId = securityUtil.getCurrentMemberId();
        MedicalCertificateResponseDto responseDto = medicalCertificateService.addMedicalCertificate(
                requestDto, childId, currentUserId);
        return CustomApiResponse.createSuccess(HttpStatus.CREATED.value(), "진단서 등록에 성공하였습니다.", responseDto);
    }


    //진단서 목록 조회
    @GetMapping("/{childId}/medical-certificates")
    public CustomApiResponse<?> getMedicalCertificates(
            @PathVariable Long childId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long currentUserId = securityUtil.getCurrentMemberId();
        MedicalCertificatePageDto responseDtos = medicalCertificateService.getMedicalCertificates(childId, currentUserId, page, size);
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), "진단서 목록 조회에 성공하였습니다.", responseDtos);
    }

    //진단서 세부 조회
    @GetMapping("{childId}/medical-certificates/{medicalCertificateId}")
    public CustomApiResponse<?> getMedicalCertificateDetail(@PathVariable Long medicalCertificateId, @PathVariable Long childId) {
        Long currentUserId = securityUtil.getCurrentMemberId();
        MedicalCertificateResponseDto responseDto = medicalCertificateService.getMedicalCertificateDetail(medicalCertificateId, childId, currentUserId);
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), "진단서 세부 조회에 성공하였습니다.", responseDto);
    }


    //미등원 확인서 등록
    @PostMapping(consumes = {"multipart/form-data"}, value = "/{childId}/absence-certificates")
    public CustomApiResponse<?> addAbsenceCertificate(
            @PathVariable Long childId,
            @Valid @ModelAttribute AbsenceCertificateRequestDto requestDto) {
        Long currentUserId = securityUtil.getCurrentMemberId();
        AbsenceCertificateResponseDto responseDto = absenceCertificateService.addAbsenceCertificate(
                requestDto, childId, currentUserId);
        return CustomApiResponse.createSuccess(HttpStatus.CREATED.value(), "미등원 확인서 등록에 성공하였습니다.", responseDto);
    }

    //미등원 확인서 목록 조회
    @GetMapping("/{childId}/absence-certificates")
    public CustomApiResponse<?> getAbsenceCertificates(
            @PathVariable Long childId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long currentUserId = securityUtil.getCurrentMemberId();
        AbsenceCertificatePageDto responseDtos = absenceCertificateService.getAbsenceCertificates(childId, currentUserId, page, size);
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), "미등원 확인서 목록 조회에 성공하였습니다.", responseDtos);
    }

    //미등원 확인서 세부 조회
    @GetMapping("{childId}/absence-certificates/{absenceCertificateId}")
    public CustomApiResponse<?> getAbsenceCertificateDetail(@PathVariable Long absenceCertificateId, @PathVariable Long childId) {
        Long currentUserId = securityUtil.getCurrentMemberId();
        AbsenceCertificateResponseDto responseDto = absenceCertificateService.getAbsenceCertificateDetail(absenceCertificateId, childId, currentUserId);
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), "미등원 확인서 세부 조회에 성공하였습니다.", responseDto);
    }

}
