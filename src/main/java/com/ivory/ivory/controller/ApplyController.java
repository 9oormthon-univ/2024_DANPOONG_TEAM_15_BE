package com.ivory.ivory.controller;

import com.ivory.ivory.dto.ApplyDto;
import com.ivory.ivory.service.ApplyService;
import com.ivory.ivory.util.SecurityUtil;
import com.ivory.ivory.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {
    private final ApplyService serviceService;
    private final SecurityUtil securityUtil;

    @PostMapping()
    public ResponseEntity<?> applyService (@Valid @RequestBody ApplyDto dto) {
        Long currentMemberId = securityUtil.getCurrentMemberId();
        CustomApiResponse<?> response = serviceService.applyService(dto,currentMemberId);
        return ResponseEntity.ok(response);
    }
}
