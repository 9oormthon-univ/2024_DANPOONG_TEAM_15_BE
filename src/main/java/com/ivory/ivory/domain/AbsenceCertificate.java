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
@Table(name="absence_certificate")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AbsenceCertificate extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="absence_certificate_id", updatable = false)
    private Long id;

    @Column(name="name", nullable = true)
    private String name;

    @Column(name="absence_start_date", nullable = true)
    private LocalDate absenceStartDate;

    @Column(name="absence_end_date", nullable = true)
    private LocalDate absenceEndDate;

    @Column(name="absence_reason", nullable = true)
    private String absenceReason;

    @Column(name="note", nullable = true)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="child_id", nullable = false)
    private Child child;

    @Builder

    public AbsenceCertificate(String name, LocalDate absenceStartDate, LocalDate absenceEndDate, String absenceReason,
                              String note, Child child) {
        this.name = name;
        this.absenceStartDate = absenceStartDate;
        this.absenceEndDate = absenceEndDate;
        this.absenceReason = absenceReason;
        this.note = note;
        this.child = child;
    }
}
