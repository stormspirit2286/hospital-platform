package com.duy.hospital.patientservice.service;

import com.duy.hospital.patientservice.dto.request.PatientRequest;
import com.duy.hospital.patientservice.dto.response.PatientResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    PatientResponse createPatient(PatientRequest patient);
    List<PatientResponse> getPatients(Pageable pageable);
    PatientResponse getPatientById(UUID patientId);
    PatientResponse updatePatient(UUID patientId, PatientRequest patient);
    void deletePatient(UUID patientId);
}
