package com.ivory.ivory.domain;

import com.ivory.ivory.dto.ApplyDto;
import com.ivory.ivory.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name="apply")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Apply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="apply_id")
    private Long id;

    @Column(name="start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name="end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name="total_amount", nullable = false)
    private Long totalAmount;

    @Column(name="subsidy", nullable = false)
    private Long subsidy;

    @Enumerated(EnumType.STRING)
    @Column(name="income_type", nullable = false)
    private IncomeType incomeType;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="child_id")
    private Child child;

    @OneToOne
    @JoinColumn(name="medical_certificate_id")
    private MedicalCertificate medicalCertificate;

    @OneToOne
    @JoinColumn(name="absence_certificate_id")
    private AbsenceCertificate absenceCertificate;

    public static Apply toEntity(
            ApplyDto service,
            Long totalAmount,
            Long subsidy,
            IncomeType incomeType,
            Status status,
            Member member,
            Child child,
            MedicalCertificate medicalCertificate,
            AbsenceCertificate absenceCertificate
    ) {
        return Apply.builder()
                .startDate(service.getStartDate())
                .endDate(service.getEndDate())
                .totalAmount(totalAmount)
                .subsidy(subsidy)
                .incomeType(incomeType)
                .status(status)
                .member(member)
                .child(child)
                .medicalCertificate(medicalCertificate)
                .absenceCertificate(absenceCertificate)
                .build();
    }
}
