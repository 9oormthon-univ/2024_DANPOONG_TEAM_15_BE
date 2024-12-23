package com.ivory.ivory.controller;

import com.ivory.ivory.dto.MemberRequestDto;
import com.ivory.ivory.dto.SignUpDto;
import com.ivory.ivory.dto.TokenDto;
import com.ivory.ivory.dto.TokenRequestDto;
import com.ivory.ivory.service.AuthService;
import com.ivory.ivory.util.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/sign-up")
    public CustomApiResponse<?> signup(@Valid @RequestBody SignUpDto signUpDto) {
        TokenDto dto = authService.signup(signUpDto);
        return CustomApiResponse.createSuccess(HttpStatus.CREATED.value(),"회원가입을 성공하였습니다.", dto);
    }

    @PostMapping("/login")
    public CustomApiResponse<TokenDto> login(@RequestBody MemberRequestDto memberRequestDto) {
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(),"로그인에 성공하였습니다.",authService.login(memberRequestDto));
    }


    @PostMapping("/reissue")
    public CustomApiResponse<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(),"토큰 재발급에 성공하였습니다.",authService.reissue(tokenRequestDto));
    }
}