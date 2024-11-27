package com.ivory.ivory.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MedicalCertificateResponseDto {
    private Long id;
    private String name;
    private String address;
    private LocalDate diagnosisDate;
    private String diagnosisName;

    @Builder
    public MedicalCertificateResponseDto(Long id, String name, String address, LocalDate diagnosisDate,
                                         String diagnosisName) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.diagnosisDate = diagnosisDate;
        this.diagnosisName = diagnosisName;
    }
}
