package com.ivory.ivory.service;

import com.ivory.ivory.domain.Member;
import com.ivory.ivory.dto.MemberInfoDto;
import com.ivory.ivory.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberInfoDto getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 회원이 존재하지 않습니다."));

        return MemberInfoDto.builder()
                .name(member.getName())
                .incomeType(member.getIncomeType().getDescription())
                .build();
    }
}
