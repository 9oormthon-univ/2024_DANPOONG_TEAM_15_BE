package com.ivory.ivory.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivory.ivory.domain.*;
import com.ivory.ivory.dto.CareDetailDto;
import com.ivory.ivory.dto.CareDto;
import com.ivory.ivory.dto.CareListDto;
import com.ivory.ivory.repository.ApplyRepository;
import com.ivory.ivory.repository.CaregiverRepository;
import com.ivory.ivory.repository.ChildRepository;
import com.ivory.ivory.util.response.CustomApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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
    private final SimpMessagingTemplate messagingTemplate;

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
                image
        );
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(),"돌봄 내용이 상세 조회되었습니다.",careDetailDto);
    }

    //돌봄 수락
    @Transactional
    public CustomApiResponse<?> AcceptCare(Long currentMemberId, Long applyId) {
        Optional<Caregiver> caregiver = caregiverRepository.findById(currentMemberId);

        if (caregiver.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 돌보미입니다.");
        }

        Optional<Apply> applyOptional = applyRepository.findById(applyId);
        if (applyOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 신청 내역입니다.");
        }

        Apply apply = applyOptional.get();

        // 상태 변경
        if (apply.getStatus() == Status.YET) {
            apply.setStatus(Status.MATCHED);
            apply.setCaregiver(caregiver.get());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 매칭된 돌봄입니다.");
        }

        applyRepository.save(apply);

        // 사용자에게 알림 전송
        String notificationMessage = "돌보미가 정해졌어요!";
        String childName = apply.getChild().getName();
        String startDate = apply.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        NotificationMessage message = NotificationMessage.builder()
                .message(notificationMessage)
                .childName(childName)
                .startDate(startDate)
                .build();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String notificationJson = objectMapper.writeValueAsString(message);

            messagingTemplate.convertAndSend(
                    "/topic/notifications/users/" + apply.getMember().getId().toString(),
                    notificationJson
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "알림 전송 중 문제가 발생했습니다.", e);
        }


        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), "돌봄이 매칭되었습니다.", null);
    }


    public CustomApiResponse<?> getMatchedCare(Long currentMemberId) {
        Optional<Caregiver> caregiver = caregiverRepository.findById(currentMemberId);
        if(caregiver.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"돌보미만 조회가 가능합니다.");
        }
        Optional <Apply> apply = applyRepository.findFirstByStatusOrderByCreateAtDesc(Status.MATCHED);
        if(apply.isEmpty()) {
            return CustomApiResponse.createSuccess(HttpStatus.OK.value(), "아직 매칭된 돌봄 활동이 없습니다.", null);
        }
        Optional<Child> child = childRepository.findById(apply.get().getChild().getId());

        //기본키
        Long applyId = apply.get().getId();

        //돌봄 날짜
        String careDate = apply.get().getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        //돌봄 시간
        String careTime = applyService.getCareTime(apply.get().getStartDate(),apply.get().getEndDate());

        //아이 이름
        String childName = child.get().getName();

        //아이 나이
        Long age = childService.calculateAge(child.get().getBirth(), LocalDate.now());

        //아이 사진
        String image = child.get().getImage();

        CareDto careDto = CareDto.of(applyId,careDate,careTime,childName,age,image);
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), "매칭된 돌봄 활동이 조회 되었습니다.",careDto);
    }
}
