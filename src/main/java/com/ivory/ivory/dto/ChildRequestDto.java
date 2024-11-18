package com.ivory.ivory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ChildRequestDto {
    @NotBlank(message = "자녀 이름은 필수 입력값입니다.")
    private String childName;

    @NotNull(message = "자녀 생년월일은 필수 입력값입니다.")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate birthDate;

    @NotBlank(message = "자녀의 교육기관은 필수 입력값입니다.")
    private String educationalInstitution;
}
