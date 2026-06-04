package com.duy.hospital.appointmentservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DepartmentRequest {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 2000)
    private String description;
}
