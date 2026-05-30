package com.duy.hospital.patientservice.dto.response;

import com.duy.hospital.patientservice.entity.enums.BenefitRate;
import com.duy.hospital.patientservice.entity.enums.InsuranceStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsuranceResponse {

    private UUID insuranceId;
    private String cardNumber;
    private String participantType;
    private String initialFacilityCode;
    private BenefitRate benefitRate;
    private LocalDate validFrom;
    private LocalDate validTo;
    private LocalDate continuousFrom;
    private InsuranceStatus status;
}

