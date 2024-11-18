package com.ivory.ivory.dto;

import com.ivory.ivory.domain.Child;
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

    public static ChildListDto from(Child child,Long age) {
        return ChildListDto.builder()
                .childId(child.getId())
                .childName(child.getName())
                .age(age)
                .build();
    }
}
