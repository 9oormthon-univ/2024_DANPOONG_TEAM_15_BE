package com.ivory.ivory.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.ivory.ivory.domain.Apply;
import com.ivory.ivory.domain.Child;
import com.ivory.ivory.domain.Member;
import com.ivory.ivory.domain.Status;
import com.ivory.ivory.dto.ChildListDto;
import com.ivory.ivory.dto.ChildRequestDto;
import com.ivory.ivory.repository.ApplyRepository;
import com.ivory.ivory.repository.ChildRepository;
import com.ivory.ivory.repository.MemberRepository;
import com.ivory.ivory.s3.S3UploadService;
import com.ivory.ivory.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.NotDirectoryException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChildService {


    private final MemberRepository memberRepository;
    private final ChildRepository childRepository;
    private final S3UploadService s3UploadService;
    private final ApplyRepository applyRepository;

    @Transactional
    public CustomApiResponse<?> addChild(ChildRequestDto dto, Long memberId) {
        try{
            //요청한 유저가 존재하는 유저인지 확인
            Optional<Member> member = memberRepository.findById(memberId);
            if(member.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.");
            }
            //이미지
            MultipartFile imageUrl = dto.getImage();
            String image = s3UploadService.upload(imageUrl,"childImage");
            //엔티티 생성
            Child child = Child.toEntity(dto,image,member.get());
            //DB에 저장
            childRepository.save(child);
            //응답 생성
            return CustomApiResponse.createSuccess(HttpStatus.CREATED.value(),"자녀 정보가 성공적으로 등록되었습니다.",null);
        } catch(DataAccessException dae) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"데이터 베이스 오류가 발생했습니다.");
        } catch (AmazonS3Exception s3Exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 중 오류가 발생했습니다.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"서버 오류가 발생했습니다.");
        }
    }

    public CustomApiResponse<?> getChildren(Long memberId) {
        //유저 확인
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.");
        }

        //자녀 확인
        List<Child> children = childRepository.findAllByMember_Id(memberId);
        if (children.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "등록한 자녀가 존재하지 않습니다.");
        }

        //응답 Dto 생성
        List<ChildListDto> childrenList = new ArrayList<>();
        children.forEach(child -> {
            LocalDate childBirthDate = child.getBirth();
            LocalDate nowDate = LocalDate.now();
            Long age = calculateAge(childBirthDate, nowDate);

            String recentApplyStatus = "아직 신청 내역이 없습니다";
            //신청 상태
            Apply apply = applyRepository.findFirstByChild_IdOrderByCreateAtDesc(child.getId());
            if (apply != null) {
                 recentApplyStatus = getStatus(apply.getStatus());
            }

            childrenList.add(ChildListDto.from(child,age,recentApplyStatus));
        });
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), "자녀 목록 조회에 성공했습니다.", childrenList);
    }

    //자녀 나이 계산 함수
    public Long calculateAge (LocalDate childBirthDate, LocalDate nowDate) {
        return (long) Period.between(childBirthDate, nowDate).getYears();
    }

    //서비스 상태
    public String getStatus(Status Status) {
        switch (Status) {
            case YET: return "서비스 신청 완료";
            case MATCHED: return "돌보미 매칭";
            case IN_PROGRESS: return "돌봄 서비스 이용 중";
            case COMPLETE: return "이용 완료";
            default: return "";
        }
    }
}

