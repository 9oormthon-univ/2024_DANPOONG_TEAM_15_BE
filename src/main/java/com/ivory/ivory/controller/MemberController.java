package com.ivory.ivory.controller;

import com.ivory.ivory.service.MemberService;
import com.ivory.ivory.util.SecurityUtil;
import com.ivory.ivory.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final SecurityUtil securityUtil;

    @GetMapping("/me")
    public CustomApiResponse<?> signup() {
        Long currentMemberId = securityUtil.getCurrentMemberId();
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), "회원 정보 조회에 성공하였습니다.",  memberService.getMemberInfo(currentMemberId));
    }
}
