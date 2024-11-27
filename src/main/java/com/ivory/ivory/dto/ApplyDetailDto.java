package com.ivory.ivory.dto;

import com.ivory.ivory.domain.Apply;
import com.ivory.ivory.domain.IncomeType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyDetailDto {
    private String applyDate;
    private String name;
    private LocalDate birthDate;
    private Long age;
    private String incomeType;
    private String careDate;
    private String careTime;
    private String memo;
    private String totalAmount;
    private String subsidy;
    private String copay;
    private String status;
    private MedicalCertificatesDto medicalCertificates;
    private AbsenceCertificatesDto absenceCertificates;

    public static ApplyDetailDto from(
            String applyDate,
            String name,
            LocalDate birthDate,
            Long age,
            String incomeType,
            String careDate,
            String careTime,
            String memo,
            String totalAmount,
            String subsidy,
            String copay,
            String status,
            MedicalCertificatesDto medicalCertificates,
            AbsenceCertificatesDto absenceCertificates
            ) {
        return ApplyDetailDto.builder()
                .applyDate(applyDate)
                .name(name)
                .birthDate(birthDate)
                .age(age)
                .incomeType(incomeType)
                .careDate(careDate)
                .careTime(careTime)
                .memo(memo)
                .totalAmount(totalAmount)
                .subsidy(subsidy)
                .copay(copay)
                .status(status)
                .medicalCertificates(medicalCertificates)
                .absenceCertificates(absenceCertificates)
                .build();
    }
}
