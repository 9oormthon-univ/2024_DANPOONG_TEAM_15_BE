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
    private String name;
    private String applyDate;
    private String careDate;
    private String careTime;
    private String status;

    public static ApplyListDto from(String name,String applyDate,String careDate,String careTime, String status) {
        return ApplyListDto.builder()
                .name(name)
                .applyDate(applyDate)
                .careDate(careDate)
                .careTime(careTime)
                .status(status)
                .build();
    }
}
