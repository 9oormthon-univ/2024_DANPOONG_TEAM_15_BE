package com.ivory.ivory.domain;

import com.ivory.ivory.util.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name="medical_certificate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MedicalCertificate extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="medical_certificate_id", updatable = false)
    private Long id;

    @Column(name="name", nullable = true)
    private String name;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "diagnosis_date", nullable = true)
    private LocalDate diagnosisDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "disease", nullable = true)
    private Disease disease;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="child_id", nullable = false)
    private Child child;

    @Builder
    public MedicalCertificate(String name, String address, LocalDate diagnosisDate, Disease disease, Child child) {
        this.name = name;
        this.address = address;
        this.diagnosisDate = diagnosisDate;
        this.disease = disease;
        this.child = child;
    }
}
