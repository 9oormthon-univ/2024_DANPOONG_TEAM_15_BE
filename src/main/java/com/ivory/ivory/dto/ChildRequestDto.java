package com.ivory.ivory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChildRequestDto {
    @NotBlank(message = "자녀 이름은 필수 입력값입니다.")
    private String childName;

    @NotBlank(message = "자녀 생년월일은 필수 입력값입니다.")
    private String birthDate;

    @NotBlank(message = "자녀의 교육기관은 필수 입력값입니다.")
    private String educationalInstitution;
}
