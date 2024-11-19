package com.ivory.ivory.dto;

import com.ivory.ivory.domain.Apply;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
