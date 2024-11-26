package com.ivory.ivory.service;

import com.ivory.ivory.domain.*;
import com.ivory.ivory.dto.ApplyListDto;
import com.ivory.ivory.dto.CareDetailDto;
import com.ivory.ivory.dto.CareListDto;
import com.ivory.ivory.repository.ApplyRepository;
import com.ivory.ivory.repository.CaregiverRepository;
import com.ivory.ivory.repository.ChildRepository;
import com.ivory.ivory.util.response.CustomApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CaregiverService {

    private final ApplyRepository applyRepository;
    private final ApplyService applyService;
    private final CaregiverRepository caregiverRepository;
    private final ChildRepository childRepository;
    private final ChildService childService;

    public CustomApiResponse<?> getCareList(Long currentMemberId) {
        Optional<Caregiver> caregiver = caregiverRepository.findById(currentMemberId);
        if(caregiver.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"돌보미만 조회가 가능합니다.");
        }
        List<Apply> applyList = applyRepository.findAll();
        List<CareListDto> careListDto = new ArrayList<>();
        applyList.forEach(apply -> {
            Long applyId = apply.getId();
            //서비스 신청 날짜
            String applyDate = applyService.getApplyDate(apply.getCreateAt());

            //서비스 이용 날짜
            String careDate = apply.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            //서비스 이용 시간
            String careTime = applyService.getCareTime(apply.getStartDate(),apply.getEndDate());

            careListDto.add(CareListDto.toCareList(applyId,applyDate,careDate,careTime));
        });
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(),"돌봄 활동 목록이 조회되었습니다.",careListDto);
    }

    public CustomApiResponse<?> getCareDetail(Long currentMemberId, Long applyId) {
        Optional<Caregiver> caregiver = caregiverRepository.findById(currentMemberId);
        if(caregiver.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"돌보미만 조회가 가능합니다.");
        }

        Optional<Apply> apply = applyRepository.findById(applyId);
        if (apply.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 신청 내역입니다.");
        }
        Optional<Child> child = childRepository.findById(apply.get().getChild().getId());
        //신청 날짜
        String applyDate = applyService.getApplyDate(apply.get().getCreateAt());
        //이용 날짜
        String careDate = apply.get().getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        //이용 시간
        String careTime = applyService.getCareTime(apply.get().getStartDate(),apply.get().getEndDate());
        //돌봄 메모
        String memo = apply.get().getMemo();
        //아이 이름
        String childName = child.get().getName();
        //아이 생년월일
        String birthDate = child.get().getBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        //나이
        Long age = childService.calculateAge(child.get().getBirth(), LocalDate.now());
        //아이 진단명
        String diagnosisName = apply.get().getMedicalCertificate().getDisease().getName();
        //진료 내용
        String diagnosisContent = apply.get().getMedicalCertificate().getDiagnosisContent();
        //아이 사진
        String image = child.get().getImage();

        //응답 생성
        CareDetailDto careDetailDto = CareDetailDto.from(
                applyDate,
                careDate,
                careTime,
                memo,
                childName,
                birthDate,
                age,
                diagnosisName,
                diagnosisContent,
                image
        );
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(),"돌봄 내용이 상세 조회되었습니다.",careDetailDto);
    }

    //돌봄 수락
    //TODO : 소켓통신
    @Transactional
    public CustomApiResponse<?> AcceptCare(Long applyId) {
        //신청 내역 조회
        Optional<Apply> applyOptional = applyRepository.findById(applyId);
        if (applyOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 신청 내역입니다.");
        }

        Apply apply = applyOptional.get();

        //상태 변경
        if (apply.getStatus() == Status.YET) {
            apply.setStatus(Status.MATCHED);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 매칭된 돌봄입니다.");
        }

        applyRepository.save(apply);

        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), "돌봄이 매칭 되었습니다.", null);
    }

}
