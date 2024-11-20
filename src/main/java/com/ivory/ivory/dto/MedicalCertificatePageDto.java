package com.ivory.ivory.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MedicalCertificatePageDto {
    private List<MedicalCertificatesDto> certificates; // 현재 페이지의 진단서 리스트
    private PageInfo pageInfo; // 페이지 정보
}
