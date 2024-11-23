package com.ivory.ivory.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyRequestDto {
    private Long applyId;

    public static ApplyRequestDto of(Long applyId) {
        return  ApplyRequestDto.builder()
                .applyId(applyId)
                .build();
    }
}
