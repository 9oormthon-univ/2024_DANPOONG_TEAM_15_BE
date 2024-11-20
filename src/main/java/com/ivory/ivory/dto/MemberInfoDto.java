package com.ivory.ivory.dto;

import com.ivory.ivory.domain.IncomeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class MemberInfoDto {
    private String name;
    private String incomeType;

    @Builder
    public MemberInfoDto(String name, String incomeType) {
        this.name = name;
        this.incomeType = incomeType;
    }
}
