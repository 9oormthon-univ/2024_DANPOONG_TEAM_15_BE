package com.ivory.ivory.service;

import com.ivory.ivory.domain.AbsenceCertificate;
import com.ivory.ivory.domain.Child;
import com.ivory.ivory.domain.Disease;
import com.ivory.ivory.domain.MedicalCertificate;
import com.ivory.ivory.dto.AbsenceCertificateRequestDto;
import com.ivory.ivory.dto.AbsenceCertificateResponseDto;
import com.ivory.ivory.dto.MedicalCertificatePageDto;
import com.ivory.ivory.dto.MedicalCertificateRequestDto;
import com.ivory.ivory.dto.MedicalCertificateResponseDto;
import com.ivory.ivory.dto.MedicalCertificatesDto;
import com.ivory.ivory.dto.PageInfo;
import com.ivory.ivory.ocr.MedicalCertificateOcrService;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    private final MedicalCertificateOcrService ocrService;

    @Qualifier("medicalCertificateOcrParser")
    private final OcrParser medicalCertificateOcrParser;
    @Value("${ocr.medical-certificate.api.url}")
    private String apiUrl;
    @Value("${ocr.medical-certificate.api.secret-key}")
    private String secretKey;

    @Transactional
    public MedicalCertificateResponseDto addMedicalCertificate(MedicalCertificateRequestDto medicalCertificateRequestDto, Long childId, Long memberId) {
        try {
            Child child = childRepository.findById(childId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Child ID"));
            if (!child.getMember().getId().equals(memberId)) {
                throw new IllegalArgumentException("본인의 자녀에 대한 진단서만 등록할 수 있습니다.");
            }

            String jsonResponse = ocrService.processOcr(medicalCertificateRequestDto.getFile(), apiUrl, secretKey);
            System.out.println(jsonResponse);
            Map<String, String> parseData = medicalCertificateOcrParser.parse(jsonResponse);

            String name = parseData.get("환자성명");
            String address = parseData.get("주소");
            String diagnosisDate = parseData.get("진단연월일");
            String diagnosisName = parseData.get("진단명");

            MedicalCertificate medicalCertificate = MedicalCertificate.builder()
                    .name(name)
                    .address(address)
                    .diagnosisDate(diagnosisDate != null ? DateUtil.parseToLocalDate(diagnosisDate) : null)
                    .disease(Disease.findByDiagnosisName(diagnosisName))
                    .child(child)
                    .build();

            medicalCertificateRepository.save(medicalCertificate);

            return MedicalCertificateResponseDto.builder()
                    .id(medicalCertificate.getId())
                    .name(medicalCertificate.getName())
                    .address(medicalCertificate.getAddress())
                    .diagnosisDate(medicalCertificate.getDiagnosisDate())
                    .diagnosisName(medicalCertificate.getDisease().getName())
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
                    .diagnosisName(medicalCertificate.getDisease().getName())
                    .build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
