package com.ivory.ivory.service;

import com.ivory.ivory.domain.AbsenceCertificate;
import com.ivory.ivory.domain.Child;
import com.ivory.ivory.domain.MedicalCertificate;
import com.ivory.ivory.dto.AbsenceCertificatePageDto;
import com.ivory.ivory.dto.AbsenceCertificateRequestDto;
import com.ivory.ivory.dto.AbsenceCertificateResponseDto;
import com.ivory.ivory.dto.AbsenceCertificatesDto;
import com.ivory.ivory.dto.MedicalCertificatePageDto;
import com.ivory.ivory.dto.MedicalCertificateResponseDto;
import com.ivory.ivory.dto.MedicalCertificatesDto;
import com.ivory.ivory.dto.PageInfo;
import com.ivory.ivory.ocr.OcrParser;
import com.ivory.ivory.ocr.OcrService;
import com.ivory.ivory.repository.AbsenceCertificateRepository;
import com.ivory.ivory.repository.ChildRepository;
import com.ivory.ivory.util.DateUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AbsenceCertificateService {
    private final AbsenceCertificateRepository absenceCertificateRepository;
    private final ChildRepository childRepository;
    private final OcrService ocrService;
    private final OcrParser certificateOcrParser;

    //TODO: 미등원 확인서 검증 로직 추가
    @Transactional
    public AbsenceCertificateResponseDto addAbsenceCertificate(
            AbsenceCertificateRequestDto absenceCertificateRequestDto, Long childId, Long memberId) {
        try {
            Child child = childRepository.findById(childId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Child ID"));
            if (!child.getMember().getId().equals(memberId)) {
                throw new IllegalArgumentException("본인의 자녀에 대한 미등원 확인서만 등록할 수 있습니다.");
            }

            String jsonResponse = ocrService.processOcr(absenceCertificateRequestDto.getFile());
            Map<String, String> parseData = certificateOcrParser.parse(jsonResponse);

            String name = parseData.get("이름"); // 없으면 null
            String startDate = parseData.get("시작일");
            String endDate = parseData.get("종료일");
            String reason = parseData.get("결석사유");
            String note = parseData.get("비고");


            AbsenceCertificate absenceCertificate = AbsenceCertificate.builder()
                    .name(name)
                    .absenceStartDate(startDate != null ? DateUtil.parseToLocalDate(startDate) : null)
                    .absenceEndDate(endDate != null ? DateUtil.parseToLocalDate(endDate) : null)
                    .absenceReason(reason)
                    .note(note)
                    .child(child)
                    .build();

            absenceCertificateRepository.save(absenceCertificate);

            return AbsenceCertificateResponseDto.builder()
                    .id(absenceCertificate.getId())
                    .name(absenceCertificate.getName())
                    .absenceStartDate(absenceCertificate.getAbsenceStartDate())
                    .absenceEndDate(absenceCertificate.getAbsenceEndDate())
                    .absenceReason(absenceCertificate.getAbsenceReason())
                    .note(absenceCertificate.getNote())
                    .build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "OCR 처리 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    public AbsenceCertificateResponseDto getAbsenceCertificateDetail(Long absenceCertificateId, Long childId, Long memberId) {
        try {
            AbsenceCertificate absenceCertificate = absenceCertificateRepository.findById(absenceCertificateId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Absence Certificate ID"));

            Child child = childRepository.findById(childId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Child ID"));

            if (!child.getMember().getId().equals(memberId)) {
                throw new IllegalArgumentException("본인의 자녀에 대한 미등원 확인서만 조회할 수 있습니다.");
            }

            return AbsenceCertificateResponseDto.builder()
                    .id(absenceCertificate.getId())
                    .name(absenceCertificate.getName())
                    .absenceStartDate(absenceCertificate.getAbsenceStartDate())
                    .absenceEndDate(absenceCertificate.getAbsenceEndDate())
                    .absenceReason(absenceCertificate.getAbsenceReason())
                    .note(absenceCertificate.getNote())
                    .build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Transactional
    public AbsenceCertificatePageDto getAbsenceCertificates(Long childId, Long memberId, int page, int size) {
        try {
            Child child = childRepository.findById(childId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Child ID"));
            if (!child.getMember().getId().equals(memberId)) {
                throw new IllegalArgumentException("본인의 자녀에 대한 미등원 확인서만 조회할 수 있습니다.");
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());
            Page<AbsenceCertificate> absenceCertificatesPage = absenceCertificateRepository.findAllByChildId(childId, pageable);

            if (absenceCertificatesPage.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "미등원 확인서가 존재하지 않습니다.");
            }
            List<AbsenceCertificatesDto> certificates = absenceCertificatesPage.getContent().stream()
                    .map(absenceCertificate -> AbsenceCertificatesDto.builder()
                            .id(absenceCertificate.getId())
                            .title(absenceCertificate.getAbsenceStartDate() != null
                                    ? absenceCertificate.getAbsenceStartDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
                                    : null)
                            .startDate(absenceCertificate.getAbsenceStartDate())
                            .endDate(absenceCertificate.getAbsenceEndDate())
                            .build())
                    .collect(Collectors.toList());

            PageInfo pageInfo = PageInfo.builder()
                    .totalPages(absenceCertificatesPage.getTotalPages())
                    .totalItems(absenceCertificatesPage.getTotalElements())
                    .currentPage(absenceCertificatesPage.getNumber())
                    .pageSize(absenceCertificatesPage.getNumberOfElements())
                    .build();

            return AbsenceCertificatePageDto.builder()
                    .certificates(certificates)
                    .pageInfo(pageInfo)
                    .build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
