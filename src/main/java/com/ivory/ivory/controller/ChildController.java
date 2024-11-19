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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/children")
@RequiredArgsConstructor
public class ChildController {
    private final ChildService childService;
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

}
