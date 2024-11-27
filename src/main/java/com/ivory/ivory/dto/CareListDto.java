package com.ivory.ivory.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CareListDto {
    private Long applyId;
    private String applyDate;
    private String careDate;
    private String careTime;

    public static CareListDto toCareList(Long applyId, String applyDate, String careDate, String careTime ) {
        return CareListDto.builder()
                .applyId(applyId)
                .applyDate(applyDate)
                .careDate(careDate)
                .careTime(careTime)
                .build();
    }
}
