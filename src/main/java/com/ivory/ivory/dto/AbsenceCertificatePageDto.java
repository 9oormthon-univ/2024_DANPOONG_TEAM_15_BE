package com.ivory.ivory.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AbsenceCertificatePageDto {
    private List<AbsenceCertificatesDto> certificates;
    private PageInfo pageInfo;
}
