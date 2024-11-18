package com.ivory.ivory.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="child")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="child_id", updatable = false)
    private Long id;

    @Column(name="child_name", nullable = false)
    private String name;

    @Column(name="child_birth", nullable = false)
    private String birth;

    @Column(name="educational_institution", nullable = false)
    private String educationalInstitution;

    @ManyToOne
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    public static Child toEntity(Member member) {
        return Child.builder()
                .member(member)
                .build();
    }
}
