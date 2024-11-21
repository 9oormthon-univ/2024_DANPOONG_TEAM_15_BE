package com.ivory.ivory.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbsenceCertificateResponseDto {
    private Long id;
    private String name;
    private LocalDate absenceStartDate;
    private LocalDate absenceEndDate;
    private String absenceReason;
    private String note;

    @Builder
    public AbsenceCertificateResponseDto(Long id, String name, LocalDate absenceStartDate, LocalDate absenceEndDate, String absenceReason, String note) {
        this.id = id;
        this.name = name;
        this.absenceStartDate = absenceStartDate;
        this.absenceEndDate = absenceEndDate;
        this.absenceReason = absenceReason;
        this.note = note;
    }
}
