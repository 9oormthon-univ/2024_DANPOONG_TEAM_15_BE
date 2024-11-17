package com.ivory.ivory.dto;

import com.ivory.ivory.domain.IncomeType;
import com.ivory.ivory.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String email;
    private IncomeType incomeType;

    public static MemberResponseDto from(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .incomeType(member.getIncomeType())
                .build();
    }
}