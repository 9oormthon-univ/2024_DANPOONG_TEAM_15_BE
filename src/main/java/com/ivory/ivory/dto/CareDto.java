package com.ivory.ivory.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CareDto {
    private Long id;
    private String careDate;
    private String careTime;
    private String childName;
    private Long age;
    private String image;

    public static CareDto of(Long id, String careDate, String careTime, String childName, Long age, String image) {
        return CareDto.builder()
                .id(id)
                .careDate(careDate)
                .careTime(careTime)
                .childName(childName)
                .age(age)
                .image(image)
                .build();
    }
}
