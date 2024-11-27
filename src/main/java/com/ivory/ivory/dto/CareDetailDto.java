package com.ivory.ivory.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CareDetailDto {
    private String applyDate;
    private String careDate;
    private String careTime;
    private String memo;
    private String childName;
    private String birthDate;
    private Long age;
    private String diagnosisName;
    private String image;

    public static CareDetailDto from (
            String applyDate,
            String careDate,
            String careTime,
            String memo,
            String childName,
            String birthDate,
            Long age,
            String diagnosisName,
            String image) {
        return CareDetailDto.builder()
                .applyDate(applyDate)
                .careDate(careDate)
                .careTime(careTime)
                .memo(memo)
                .childName(childName)
                .birthDate(birthDate)
                .age(age)
                .diagnosisName(diagnosisName)
                .image(image)
                .build();
    }
}

