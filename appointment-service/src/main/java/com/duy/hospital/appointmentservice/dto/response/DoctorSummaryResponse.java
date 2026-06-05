package com.duy.hospital.appointmentservice.dto.response;

import com.duy.hospital.appointmentservice.entity.enums.DoctorStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorSummaryResponse {

    private UUID doctorId;
    private UUID userId;
    private UUID departmentId;
    private String departmentName;
    private String fullName;
    private String email;
    private String phone;
    private String specialization;
    private String licenseNumber;
    private DoctorStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
