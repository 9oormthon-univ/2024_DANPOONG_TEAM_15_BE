package com.ivory.ivory.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildListDto {
    private Long childId;
    private String childName;
    private Long age;
    //TODO : recentService 추가 예정
}
