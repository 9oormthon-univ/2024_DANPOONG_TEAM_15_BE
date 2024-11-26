package com.ivory.ivory.controller;

import com.ivory.ivory.service.CaregiverService;
import com.ivory.ivory.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/caregivers")
@RequiredArgsConstructor
public class CaregiverController {

    private final CaregiverService caregiverService;

    @GetMapping("/list")
    public ResponseEntity<?> getCareList () {
        CustomApiResponse<?> response =caregiverService.getCareList();
        return ResponseEntity.ok(response);
    }
}
