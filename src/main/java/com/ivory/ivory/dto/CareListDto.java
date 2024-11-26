package com.ivory.ivory.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CareListDto {
    private String applyDate;
    private String careDate;
    private String careTime;

    public static CareListDto toCareList(String applyDate, String careDate, String careTime ) {
        return CareListDto.builder()
                .applyDate(applyDate)
                .careDate(careDate)
                .careTime(careTime)
                .build();
    }
}
