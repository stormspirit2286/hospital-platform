package com.duy.hospital.patientservice.entity;

import com.duy.hospital.patientservice.entity.enums.BenefitRate;
import com.duy.hospital.patientservice.entity.enums.InsuranceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "patient_insurances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientInsurance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "insurance_id")
    private UUID insuranceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    @Column(name = "card_number", nullable = false, unique = true, length = 15)
    private String cardNumber;

    @Column(name = "participant_type", length = 2)
    private String participantType;

    @Column(name = "initial_facility_code", length = 20)
    private String initialFacilityCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "benefit_rate", length = 20)
    private BenefitRate benefitRate;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "continuous_from")
    private LocalDate continuousFrom;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private InsuranceStatus status;
}
