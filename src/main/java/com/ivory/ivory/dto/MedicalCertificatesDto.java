package com.ivory.ivory.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MedicalCertificatesDto {
    private Long id;
    private String title;
    private LocalDate createdAt;

    @Builder
    public MedicalCertificatesDto(Long id, String title, LocalDate createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }
}
