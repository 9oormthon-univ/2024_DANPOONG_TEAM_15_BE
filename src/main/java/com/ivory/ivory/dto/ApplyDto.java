package com.ivory.ivory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ApplyDto {

    @NotNull(message = "신청할 자식의 기본키는 필수값 입니다.")
    private Long childId;

    @NotNull(message = "진단서의 기본키는 필수값 입니다.")
    private  Long medicalCertificateId;

    @NotNull(message = "미등원 확인서의 기본키는 필수값 입니다.")
    private Long absenceCertificateId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}
