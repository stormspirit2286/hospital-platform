package com.duy.hospital.appointmentservice.dto.response;

import com.duy.hospital.appointmentservice.entity.enums.DoctorScheduleStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorScheduleResponse {

    private UUID scheduleId;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotMinutes;
    private DoctorScheduleStatus status;
}
