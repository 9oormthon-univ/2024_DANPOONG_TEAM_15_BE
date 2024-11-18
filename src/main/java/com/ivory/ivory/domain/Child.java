package com.ivory.ivory.domain;

import com.ivory.ivory.dto.ChildRequestDto;
import com.ivory.ivory.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="child")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Child extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="child_id", updatable = false)
    private Long id;

    @Column(name="child_name", nullable = false)
    private String name;

    @Column(name="child_birth", nullable = false)
    private LocalDate birth;

    @Column(name="educational_institution", nullable = false)
    private String educationalInstitution;

    @ManyToOne
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    public static Child toEntity(ChildRequestDto dto, Member member) {
        if (dto == null || member == null) {
            throw new IllegalArgumentException("dto와 member는 null일 수 없습니다.");
        }
        return Child.builder()
                .name(dto.getChildName())
                .birth(dto.getBirthDate())
                .educationalInstitution(dto.getEducationalInstitution())
                .member(member)
                .build();
    }
}
