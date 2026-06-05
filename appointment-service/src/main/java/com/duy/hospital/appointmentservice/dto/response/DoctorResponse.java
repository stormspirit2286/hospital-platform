package com.duy.hospital.appointmentservice.dto.response;

import com.duy.hospital.appointmentservice.entity.enums.DoctorStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorResponse {

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
    private List<DoctorScheduleResponse> schedules;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
