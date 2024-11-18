package com.ivory.ivory.service;

import com.ivory.ivory.domain.Child;
import com.ivory.ivory.domain.Member;
import com.ivory.ivory.dto.ChildListDto;
import com.ivory.ivory.dto.ChildRequestDto;
import com.ivory.ivory.repository.ChildRepository;
import com.ivory.ivory.repository.MemberRepository;
import com.ivory.ivory.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public CustomApiResponse<?> addChild(ChildRequestDto dto, Long memberId) {
        //요청한 유저가 존재하는 유저인지 확인
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.");
        }
        //엔티티 생성
        Child child = Child.toEntity(dto,member.get());
        //DB에 저장
        childRepository.save(child);
        //응답 생성
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(),"자녀 정보가 성공적으로 등록되었습니다.",null);
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
            Long age = (long) Period.between(childBirthDate, nowDate).getYears();
            childrenList.add(ChildListDto.from(child, age));
        });
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(), "자녀 목록 조회에 성공했습니다.", childrenList);
    }
}

