package com.ivory.ivory.dto;

import com.ivory.ivory.domain.Apply;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
