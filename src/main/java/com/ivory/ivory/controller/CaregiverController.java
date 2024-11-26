package com.ivory.ivory.controller;

import com.ivory.ivory.service.CaregiverService;
import com.ivory.ivory.util.SecurityUtil;
import com.ivory.ivory.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/caregivers")
@RequiredArgsConstructor
public class CaregiverController {

    private final CaregiverService caregiverService;
    private final SecurityUtil securityUtil;

    @GetMapping("/list")
    public ResponseEntity<?> getCareList () {
        Long currentMemberId = securityUtil.getCurrentMemberId();
        CustomApiResponse<?> response =caregiverService.getCareList(currentMemberId);
        return ResponseEntity.ok(response);
    }
}
