package com.ivory.ivory.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbsenceCertificatesDto {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public AbsenceCertificatesDto(Long id, String title, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
