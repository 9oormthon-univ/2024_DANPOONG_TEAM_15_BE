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
    private String gender;
    private String image;
    private String recentApplyStatus;

    public static ChildListDto from(Child child,Long age, String recentApplyStatus) {
        return ChildListDto.builder()
                .childId(child.getId())
                .childName(child.getName())
                .age(age)
                .gender(child.getGender())
                .image(child.getImage())
                .recentApplyStatus(recentApplyStatus)
                .build();
    }
}
