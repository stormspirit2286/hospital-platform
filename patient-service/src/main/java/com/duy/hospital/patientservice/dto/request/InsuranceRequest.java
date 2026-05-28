package com.duy.hospital.patientservice.dto.request;

import com.duy.hospital.patientservice.entity.enums.BenefitRate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceRequest {

    @NotBlank(message = "Card number is required")
    @Pattern(
            regexp = "^[A-Z]{2}[0-9]{13}$",
            message = "Invalid health insurance card number format"
    )
    @Size(max = 15, message = "Card number must not exceed 15 characters")
    private String cardNumber;

    @Size(max = 2, message = "Participant type must not exceed 2 characters")
    private String participantType;

    @Size(max = 20, message = "Initial facility code must not exceed 20 characters")
    private String initialFacilityCode;

    private BenefitRate benefitRate;

    private LocalDate validFrom;
    private LocalDate validTo;
    private LocalDate continuousFrom;
}
