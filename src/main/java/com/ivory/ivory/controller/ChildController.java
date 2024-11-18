package com.ivory.ivory.controller;

import com.ivory.ivory.dto.ChildRequestDto;
import com.ivory.ivory.service.AuthService;
import com.ivory.ivory.service.ChildService;
import com.ivory.ivory.service.CustomUserDetailsService;
import com.ivory.ivory.util.SecurityUtil;
import com.ivory.ivory.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/children")
@RequiredArgsConstructor
public class ChildController {
    private final ChildService childService;
    private final SecurityUtil securityUtil;

    //자녀 등록
    @PostMapping()
    public ResponseEntity<?> addChild(@Valid @RequestBody ChildRequestDto dto) {
        Long currentUserId = securityUtil.getCurrentMemberId();
        CustomApiResponse<?> result = childService.addChild(dto,currentUserId);
        return ResponseEntity.ok(result);
    }

}
