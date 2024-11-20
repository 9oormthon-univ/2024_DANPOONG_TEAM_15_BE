package com.ivory.ivory.domain;

import com.ivory.ivory.util.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "diagnosis_date", nullable = false)
    private LocalDate diagnosisDate;

    @Column(name = "diagnosis_name", nullable = false)
    private String diagnosisName;

    @Column(name = "diagnosis_content", nullable = false)
    private String diagnosisContent;

    @Column(name = "doctor_name", nullable = false)
    private String doctorName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="child_id", nullable = false)
    private Child child;

    @Builder
    public MedicalCertificate(String name, String address, LocalDate diagnosisDate, String diagnosisName, String diagnosisContent, String doctorName, Child child) {
        this.name = name;
        this.address = address;
        this.diagnosisDate = diagnosisDate;
        this.diagnosisName = diagnosisName;
        this.diagnosisContent = diagnosisContent;
        this.doctorName = doctorName;
        this.child = child;
    }
}
