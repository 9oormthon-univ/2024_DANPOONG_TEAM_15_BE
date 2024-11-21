package com.ivory.ivory.service;

import com.ivory.ivory.domain.AbsenceCertificate;
import com.ivory.ivory.domain.Child;
import com.ivory.ivory.domain.MedicalCertificate;
import com.ivory.ivory.dto.AbsenceCertificateRequestDto;
import com.ivory.ivory.dto.AbsenceCertificateResponseDto;
import com.ivory.ivory.dto.MedicalCertificatePageDto;
import com.ivory.ivory.dto.MedicalCertificateRequestDto;
import com.ivory.ivory.dto.MedicalCertificateResponseDto;
import com.ivory.ivory.dto.MedicalCertificatesDto;
import com.ivory.ivory.dto.PageInfo;
import com.ivory.ivory.ocr.OcrParser;
import com.ivory.ivory.ocr.OcrService;
import com.ivory.ivory.repository.AbsenceCertificateRepository;
import com.ivory.ivory.repository.ChildRepository;
import com.ivory.ivory.repository.MedicalCertificateRepository;
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
public class MedicalCertificateService {
    private final MedicalCertificateRepository medicalCertificateRepository;
    private final ChildRepository childRepository;
    private final OcrService ocrService;
    private final OcrParser certificateOcrParser;

    //TODO: 진단서 검증 로직 추가
    @Transactional
    public MedicalCertificateResponseDto addMedicalCertificate(MedicalCertificateRequestDto medicalCertificateRequestDto, Long childId, Long memberId) {
        try {
            Child child = childRepository.findById(childId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Child ID"));
            if (!child.getMember().getId().equals(memberId)) {
                throw new IllegalArgumentException("본인의 자녀에 대한 진단서만 등록할 수 있습니다.");
            }

            String jsonResponse = ocrService.processOcr(medicalCertificateRequestDto.getFile());
            Map<String, String> parseData = certificateOcrParser.parse(jsonResponse);

            String name = parseData.get("이름");
            String address = parseData.get("주소");
            String diagnosisDate = parseData.get("진단일");
            String diagnosisName = parseData.get("진단명");
            String diagnosisContent = parseData.get("의견");
            String doctorName = parseData.get("의사명");


            MedicalCertificate medicalCertificate = MedicalCertificate.builder()
                    .name(name)
                    .address(address)
                    .diagnosisDate(diagnosisDate != null ? DateUtil.parseToLocalDate(diagnosisDate) : null)
                    .diagnosisName(diagnosisName)
                    .diagnosisContent(diagnosisContent)
                    .doctorName(doctorName)
                    .child(child)
                    .build();

            medicalCertificateRepository.save(medicalCertificate);

            return MedicalCertificateResponseDto.builder()
                    .id(medicalCertificate.getId())
                    .name(medicalCertificate.getName())
                    .address(medicalCertificate.getAddress())
                    .diagnosisDate(medicalCertificate.getDiagnosisDate())
                    .diagnosisName(medicalCertificate.getDiagnosisName())
                    .diagnosisContent(medicalCertificate.getDiagnosisContent())
                    .doctorName(medicalCertificate.getDoctorName())
                    .build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "OCR 처리 중 오류가 발생했습니다.");
        }
    }

    @Transactional
    public MedicalCertificatePageDto getMedicalCertificates(Long childId, Long memberId, int page, int size) {
        try {
            Child child = childRepository.findById(childId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Child ID"));
            if (!child.getMember().getId().equals(memberId)) {
                throw new IllegalArgumentException("본인의 자녀에 대한 진단서만 조회할 수 있습니다.");
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by("createAt").descending());
            Page<MedicalCertificate> medicalCertificatesPage = medicalCertificateRepository.findAllByChildId(childId, pageable);

            if (medicalCertificatesPage.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "의료기록이 존재하지 않습니다.");
            }
            List<MedicalCertificatesDto> certificates = medicalCertificatesPage.getContent().stream()
                    .map(medicalCertificate -> MedicalCertificatesDto.builder()
                            .id(medicalCertificate.getId())
                            .title(medicalCertificate.getDiagnosisDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")))
                            .createdAt(LocalDate.from(medicalCertificate.getCreateAt()))
                            .build())
                    .collect(Collectors.toList());

            PageInfo pageInfo = PageInfo.builder()
                    .totalPages(medicalCertificatesPage.getTotalPages())
                    .totalItems(medicalCertificatesPage.getTotalElements())
                    .currentPage(medicalCertificatesPage.getNumber())
                    .pageSize(medicalCertificatesPage.getNumberOfElements())
                    .build();

            return MedicalCertificatePageDto.builder()
                    .certificates(certificates)
                    .pageInfo(pageInfo)
                    .build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Transactional
    public MedicalCertificateResponseDto getMedicalCertificateDetail(Long medicalCertificateId, Long childId, Long memberId) {
        try {

            MedicalCertificate medicalCertificate = medicalCertificateRepository.findById(medicalCertificateId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Medical Certificate ID"));

            Child child = childRepository.findById(childId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Child ID"));

            if (!child.getMember().getId().equals(memberId)) {
                throw new IllegalArgumentException("본인의 자녀에 대한 진단서만 조회할 수 있습니다.");
            }

            return MedicalCertificateResponseDto.builder()
                    .id(medicalCertificate.getId())
                    .name(medicalCertificate.getName())
                    .address(medicalCertificate.getAddress())
                    .diagnosisDate(medicalCertificate.getDiagnosisDate())
                    .diagnosisName(medicalCertificate.getDiagnosisName())
                    .diagnosisContent(medicalCertificate.getDiagnosisContent())
                    .doctorName(medicalCertificate.getDoctorName())
                    .build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
