package com.ivory.ivory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MedicalCertificateRequestDto {
    @NotNull(message = "파일이 필요합니다.")
    private MultipartFile file;  // 업로드할 파일
}