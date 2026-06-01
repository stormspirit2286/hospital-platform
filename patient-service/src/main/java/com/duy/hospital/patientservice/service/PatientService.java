package com.duy.hospital.patientservice.service;

import com.duy.hospital.patientservice.dto.request.PatientRequest;
import com.duy.hospital.patientservice.dto.request.PatientUpdateRequest;
import com.duy.hospital.patientservice.dto.response.PageResponse;
import com.duy.hospital.patientservice.dto.response.PatientResponse;
import com.duy.hospital.patientservice.dto.response.PatientSummaryResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    PatientResponse createPatient(PatientRequest patient);
    PageResponse<PatientResponse> getPatients(Pageable pageable);
    PageResponse<PatientSummaryResponse> getPatientSummaries(Pageable pageable);
    PatientResponse getPatientById(UUID patientId);
    PatientResponse updatePatient(UUID patientId, PatientUpdateRequest request);
    void deletePatient(UUID patientId);
}
