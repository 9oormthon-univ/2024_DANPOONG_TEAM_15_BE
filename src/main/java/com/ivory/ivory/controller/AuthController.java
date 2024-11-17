package com.ivory.ivory.controller;

import com.ivory.ivory.dto.MemberRequestDto;
import com.ivory.ivory.dto.MemberResponseDto;
import com.ivory.ivory.dto.SignUpDto;
import com.ivory.ivory.dto.TokenDto;
import com.ivory.ivory.dto.TokenRequestDto;
import com.ivory.ivory.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(authService.signup(signUpDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.login(memberRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }
}