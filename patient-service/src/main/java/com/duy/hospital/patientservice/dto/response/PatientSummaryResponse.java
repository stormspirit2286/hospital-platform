package com.duy.hospital.patientservice.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientSummaryResponse {

    private UUID patientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String email;
    private String city;
    private String status;
}
