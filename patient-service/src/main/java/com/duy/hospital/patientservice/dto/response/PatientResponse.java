package com.duy.hospital.patientservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientResponse {

    private UUID patientId;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String address;
    private String city;
    private String status;

    private InsuranceResponse insurance;
    private List<EmergencyContactResponse> emergencyContacts;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
