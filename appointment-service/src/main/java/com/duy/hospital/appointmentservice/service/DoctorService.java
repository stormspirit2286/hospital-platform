package com.duy.hospital.appointmentservice.service;

import com.duy.hospital.appointmentservice.dto.request.DoctorRequest;
import com.duy.hospital.appointmentservice.dto.response.DoctorResponse;
import com.duy.hospital.appointmentservice.dto.response.DoctorSummaryResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorService {
    DoctorResponse createDoctor(DoctorRequest request);
    DoctorResponse updateDoctor(UUID doctorId, DoctorRequest request);
    void deleteDoctor(UUID doctorId);
    DoctorResponse getDoctorById(UUID doctorId);
    List<DoctorSummaryResponse> getDoctorsByDepartment(UUID departmentId);
}
