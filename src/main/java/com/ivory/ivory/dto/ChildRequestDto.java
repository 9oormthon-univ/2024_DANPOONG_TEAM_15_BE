package com.ivory.ivory.dto;

import jakarta.validation.constraints.*;
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
    @Size(max = 50, message = "자녀 이름은 50자를 초과할 수 없습니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z\\s]+$", message = "자녀 이름은 한글, 영문, 공백만 허용됩니다.")
    private String childName;

    @NotNull(message = "자녀 생년월일은 필수 입력값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "생년월일은 현재 날짜보다 이전이어야 합니다.")
    private LocalDate birthDate;

    @NotBlank(message = "자녀의 교육기관은 필수 입력값입니다.")
    @Size(max = 100, message = "교육기관명은 100자를 초과할 수 없습니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]+$", message = "교육기관명은 한글, 영문, 숫자, 공백만 허용됩니다.")
    private String educationalInstitution;
}