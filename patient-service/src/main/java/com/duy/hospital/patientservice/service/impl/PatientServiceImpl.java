package com.duy.hospital.patientservice.service.impl;

import com.duy.hospital.patientservice.dto.request.PatientRequest;
import com.duy.hospital.patientservice.dto.request.PatientUpdateRequest;
import com.duy.hospital.patientservice.dto.response.PageResponse;
import com.duy.hospital.patientservice.dto.response.PatientResponse;
import com.duy.hospital.patientservice.dto.response.PatientSummaryResponse;
import com.duy.hospital.patientservice.dto.response.ResponseCode;
import com.duy.hospital.patientservice.entity.EmergencyContact;
import com.duy.hospital.patientservice.entity.Patient;
import com.duy.hospital.patientservice.entity.PatientInsurance;
import com.duy.hospital.patientservice.entity.enums.InsuranceStatus;
import com.duy.hospital.patientservice.exception.AppException;
import com.duy.hospital.patientservice.mapper.PatientMapper;
import com.duy.hospital.patientservice.repository.PatientRepository;
import com.duy.hospital.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    @Transactional
    public PatientResponse createPatient(PatientRequest patientRequest) {
        log.info("creating new Patient - {} {}", patientRequest.getFirstName(), patientRequest.getLastName());
        Patient patient = patientMapper.toEntity(patientRequest);
        if (patientRequest.getInsurance() != null) {
            PatientInsurance patientInsurance = patientMapper.toEntity(patientRequest.getInsurance());
            patientInsurance.setPatient(patient);
            patientInsurance.setStatus(InsuranceStatus.ACTIVE);
            patient.setInsurance(patientInsurance);
        }

        if (patientRequest.getEmergencyContacts() != null) {
            List<EmergencyContact> emergencyContacts = patientRequest.getEmergencyContacts().stream()
                    .map(contact -> {
                        EmergencyContact emergencyContact = patientMapper.toEntity(contact);
                        emergencyContact.setPatient(patient);
                        return emergencyContact;
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
            patient.setEmergencyContacts(emergencyContacts);
        }
        Patient patientCreated = patientRepository.save(patient);
        log.info("Created Patient - {}", patientCreated.getPatientId());
        return patientMapper.toResponse(patientCreated);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PatientResponse> getPatients(Pageable pageable) {
        Page<Patient> patients = patientRepository.findAllWithInsurance(pageable);

        List<UUID> ids = patients.getContent().stream()
                .map(Patient::getPatientId)
                .toList();

        if (!ids.isEmpty()) {
            patientRepository.findAllWithEmergencyContacts(ids);
        }

        return PageResponse.<PatientResponse>builder()
                .content(patients.getContent().stream()
                        .map(patientMapper::toResponse)
                        .collect(Collectors.toList()))
                .page(patients.getNumber())
                .size(patients.getSize())
                .totalElements(patients.getTotalElements())
                .totalPages(patients.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PatientSummaryResponse> getPatientSummaries(Pageable pageable) {
        Page<PatientSummaryResponse> page = patientRepository.findAllSummaries(pageable);
        return PageResponse.<PatientSummaryResponse>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getPatientById(UUID patientId) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new AppException(ResponseCode.PATIENT_NOT_FOUND));
        return patientMapper.toResponse(patient);
    }

    @Override
    @Transactional
    public PatientResponse updatePatient(UUID patientId, PatientUpdateRequest request) {
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new AppException(ResponseCode.PATIENT_NOT_FOUND));
        patientMapper.updateEntity(request, patient);
        return patientMapper.toResponse(patient);
    }

    @Override
    @Transactional
    public void deletePatient(UUID patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new AppException(ResponseCode.PATIENT_NOT_FOUND);
        }
        patientRepository.deleteById(patientId);
    }
}
