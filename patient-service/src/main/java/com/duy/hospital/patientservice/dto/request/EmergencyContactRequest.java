package com.duy.hospital.patientservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyContactRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 150, message = "Full name must not exceed 150 characters")
    private String fullName;

    @Size(max = 80, message = "Relationship must not exceed 80 characters")
    private String relationship;

    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^(0|\\+84)[0-9]{9,10}$",
            message = "Invalid phone number"
    )
    @Size(max = 30, message = "Phone must not exceed 30 characters")
    private String phone;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
}
