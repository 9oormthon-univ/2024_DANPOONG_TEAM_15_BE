package com.ivory.ivory.dto;

import com.ivory.ivory.domain.Apply;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//TODO : 진단서, 미등원 확인서 등록 서류 추가 필요
public class ApplyListDto {
    private String applyDate;
    private String careTime;
    private String status;

    public static ApplyListDto from(String applyDate, String careTime, String status) {
        return ApplyListDto.builder()
                .applyDate(applyDate)
                .careTime(careTime)
                .status(status)
                .build();
    }
}
