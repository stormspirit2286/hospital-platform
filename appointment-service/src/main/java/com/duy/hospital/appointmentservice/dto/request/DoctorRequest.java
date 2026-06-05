package com.duy.hospital.appointmentservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class DoctorRequest {

    @NotNull
    private UUID departmentId;

    @NotBlank
    @Size(max = 150)
    private String fullName;

    @Email
    @Size(max = 150)
    private String email;

    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Invalid phone number")
    @Size(max = 30)
    private String phone;

    @Size(max = 150)
    private String specialization;

    @Size(max = 100)
    private String licenseNumber;
}
