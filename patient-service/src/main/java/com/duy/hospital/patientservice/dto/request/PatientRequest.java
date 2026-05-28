package com.duy.hospital.patientservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequest {

    private UUID userId;

    @NotBlank(message = "First name is required")
    @Size(max = 80, message = "First name must not exceed 80 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 80, message = "Last name must not exceed 80 characters")
    private String lastName;

    @Size(max = 20, message = "Gender must not exceed 20 characters")
    private String gender;

    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^(0|\\+84)[0-9]{9,10}$",
            message = "Invalid phone number"
    )
    @Size(max = 30, message = "Phone must not exceed 30 characters")
    private String phone;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Valid
    @Size(max = 2, message = "Maximum 2 emergency contacts allowed")
    private List<EmergencyContactRequest> emergencyContacts;

    @Valid
    private InsuranceRequest insurance;
}
